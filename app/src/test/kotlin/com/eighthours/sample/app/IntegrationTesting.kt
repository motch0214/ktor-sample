package com.eighthours.sample.app

import com.eighthours.sample.app.impl.DefaultInitialization
import com.eighthours.sample.app.mock.DateTimeSupportMock
import com.eighthours.sample.app.module.Modules
import com.eighthours.sample.app.module.installNegotiation
import com.eighthours.sample.app.module.installRouting
import com.eighthours.sample.app.module.installSecurity
import com.eighthours.sample.app.support.Initialization
import com.eighthours.sample.domain.common.StringId
import com.eighthours.sample.domain.common.tx
import com.eighthours.sample.domain.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.test.KoinTest
import org.koin.test.mock.MockProvider
import org.koin.test.mock.declareMock
import org.koin.test.mock.declareModule
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import java.time.Duration
import java.time.OffsetDateTime
import java.util.*

fun KoinTest.testUsecase(at: OffsetDateTime? = null, module: Module? = null, test: TestApplicationEngine.() -> Unit) {
    try {
        withTestApplication({
            startKoin {
                modules(Modules)
                if (module != null) {
                    modules(module)
                }

                MockProvider.register { mock(it.java) }
                declareMock<FirebaseAuth> {
                    given(verifyIdToken(any())).willAnswer {
                        val args = Token.decode(it.arguments[0] as String)
                        mock(FirebaseToken::class.java).also { token ->
                            given(token.uid).willReturn(args.id.value)
                            given(token.name).willReturn(args.name)
                        }
                    }
                }
                declareModule {
                    if (at != null) {
                        single { DateTimeSupportMock.at(at) }
                    }
                    single<Initialization>(createdAtStart = true) { CleanInitialization() }
                }
            }

            installNegotiation()
            installSecurity()
            installRouting()
        }, test)
    } finally {
        stopKoin()
    }
}

fun <R> TestApplicationEngine.withGet(
    uri: String, token: Token? = null,
    assertions: TestApplicationCall.() -> R
): R {
    return with(handleRequest(HttpMethod.Get, uri) {
        if (token != null) {
            addHeader(HttpHeaders.Authorization, "Bearer ${token.encode()}")
        }
    }, assertions)
}

fun <R> TestApplicationEngine.withPost(
    uri: String, request: String, token: Token? = null,
    assertions: TestApplicationCall.() -> R
): R {
    return with(handleRequest(HttpMethod.Post, uri) {
        if (token != null) {
            addHeader(HttpHeaders.Authorization, "Bearer ${token.encode()}")
        }
        addHeader(HttpHeaders.ContentType, "application/json")
        setBody(request)
    }, assertions)
}

fun KoinTest.at(time: OffsetDateTime) {
    declareModule {
        single { DateTimeSupportMock.at(time) }
    }
}

val Int.hours: Duration get() = Duration.ofHours(this.toLong())

fun TestApplicationResponse.ok(): TestApplicationResponse {
    assertThat(status()).isEqualTo(HttpStatusCode.OK)
    return this
}

inline fun <reified E> TestApplicationResponse.content(): E {
    assertThat(content).isNotNull
    return Json.decodeFromString(content!!)
}

@Serializable
data class Token(val id: StringId<User>, val name: String? = null) {

    companion object {
        private fun String.encodeBase64(): String = Base64.getEncoder().encodeToString(toByteArray())
        private fun String.decodeBase64(): String = String(Base64.getDecoder().decode(this))
        private fun String.decodeJson(): Token = Json.decodeFromString(serializer(), this)
        fun decode(value: String): Token = value.decodeBase64().decodeJson()
    }

    private fun encodeJson(): String = Json.encodeToString(serializer(), this)
    fun encode(): String = encodeJson().encodeBase64()
}

class CleanInitialization : Initialization, KoinComponent {
    init {
        val flyway = DefaultInitialization.createFlyway()
        runBlocking {
            tx.required {
                flyway.clean()
                flyway.migrate()
            }
        }
    }
}
