package com.eighthours.sample.app.support

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseToken
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.auth.*
import io.ktor.response.*
import io.ktor.util.pipeline.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

private val AuthKey: Any = "FirebaseAuth"

private val log: Logger = LoggerFactory.getLogger("FirebaseAuth")

/**
 * Represents a principal consist of the specified [FirebaseToken]
 * @param token FirebaseToken
 */
class FirebasePrincipal(val token: FirebaseToken) : Principal

/**
 * Firebase Authentication provider that will be registered with the specified [name]
 */
class FirebaseAuthenticationProvider internal constructor(config: Configuration) :
    AuthenticationProvider(config) {

    internal val firebase: FirebaseAuth = config.firebase ?: FirebaseAuth.getInstance()
    internal val realm: String = config.realm
    internal val scheme: String = config.scheme
    internal val authHeader: (ApplicationCall) -> HttpAuthHeader? = config.authHeader
    internal val authenticationFunction: AuthenticationFunction<FirebaseToken> = config.authenticationFunction
    internal val challengeFunction: ChallengeFunction = config.challenge

    /**
     * Firebase Authentication provider configuration
     */
    class Configuration internal constructor(name: String?) : AuthenticationProvider.Configuration(name) {

        internal var authenticationFunction: AuthenticationFunction<FirebaseToken> = {
            FirebasePrincipal(
                it
            )
        }

        internal var authHeader: (ApplicationCall) -> HttpAuthHeader? = { call ->
            try {
                call.request.parseAuthorizationHeader()
            } catch (ex: IllegalArgumentException) {
                log.debug("Illegal HTTP auth header", ex)
                null
            }
        }

        internal var challenge: ChallengeFunction = { scheme, realm ->
            call.respond(
                UnauthorizedResponse(
                    HttpAuthHeader.Parameterized(
                        scheme,
                        mapOf(HttpAuthHeader.Parameters.Realm to realm)
                    )
                )
            )
        }

        /**
         * Firebase Authentication
         */
        var firebase: FirebaseAuth? = null

        /**
         * JWT realm name that will be used during auth challenge
         */
        var realm: String = "Ktor Server"

        /**
         * Scheme that will be used to challenge the client when no valid auth is provided
         */
        var scheme = "Bearer"

        /**
         * Http auth header retrieval function. By default it does parse `Authorization` header content.
         */
        fun authHeader(block: (ApplicationCall) -> HttpAuthHeader?) {
            authHeader = block
        }

        /**
         * Apply [validate] function to every call with [FirebaseToken]
         * @return a principal (usually an instance of [FirebaseToken]) or `null`
         */
        fun validate(validate: AuthenticationFunction<FirebaseToken>) {
            authenticationFunction = validate
        }

        /**
         * Specifies what to send back if jwt authentication fails.
         */
        fun challenge(block: ChallengeFunction) {
            challenge = block
        }

        internal fun build() = FirebaseAuthenticationProvider(this)
    }
}

/**
 * Specifies what to send back if session authentication fails.
 */
typealias ChallengeFunction = suspend PipelineContext<*, ApplicationCall>.(defaultScheme: String, realm: String) -> Unit

/**
 * Installs Firebase Authentication mechanism
 */
fun Authentication.Configuration.firebase(
    name: String? = null,
    configure: FirebaseAuthenticationProvider.Configuration.() -> Unit
) {
    val provider = FirebaseAuthenticationProvider.Configuration(name).apply(configure).build()

    provider.pipeline.intercept(AuthenticationPipeline.RequestAuthentication) { context ->
        val token = provider.authHeader(call)
        if (token == null) {
            context.bearerChallenge(
                AuthenticationFailedCause.NoCredentials,
                provider.realm, provider.scheme, provider.challengeFunction
            )
            return@intercept
        }

        try {
            val principal = verifyAndValidate(
                call, token,
                provider.scheme, provider.firebase, provider.authenticationFunction
            )
            if (principal != null) {
                context.principal(principal)
            } else {
                context.bearerChallenge(
                    AuthenticationFailedCause.InvalidCredentials,
                    provider.realm, provider.scheme, provider.challengeFunction
                )
            }
        } catch (cause: Throwable) {
            val message = cause.message ?: cause.javaClass.simpleName
            log.debug("JWT verification failed: $message")
            context.error(AuthKey, AuthenticationFailedCause.Error(message))
        }
    }

    register(provider)
}

private fun AuthenticationContext.bearerChallenge(
    cause: AuthenticationFailedCause,
    realm: String,
    scheme: String,
    challengeFunction: ChallengeFunction
) = challenge(AuthKey, cause) {
    challengeFunction(this, scheme, realm)
    if (!it.completed && call.response.status() != null) {
        it.complete()
    }
}

private suspend fun verifyAndValidate(
    call: ApplicationCall,
    token: HttpAuthHeader,
    scheme: String,
    firebase: FirebaseAuth,
    validate: suspend ApplicationCall.(FirebaseToken) -> Principal?
): Principal? {
    val blob = if (token is HttpAuthHeader.Single && token.authScheme == scheme) {
        token.blob
    } else {
        return null
    }

    val credentials = try {
        firebase.verifyIdToken(blob)
    } catch (ex: FirebaseAuthException) {
        log.debug("Token verification failed: ${ex.message}")
        null
    } ?: return null

    return validate(call, credentials)
}
