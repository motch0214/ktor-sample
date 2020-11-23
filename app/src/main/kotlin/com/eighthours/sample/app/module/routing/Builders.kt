package com.eighthours.sample.app.module.routing

import com.eighthours.sample.app.support.FirebasePrincipal
import com.eighthours.sample.domain.common.StringId
import com.eighthours.sample.usecase.CommandUsecase
import com.eighthours.sample.usecase.Usecase
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun Route.public(build: Route.() -> Unit) {
    authenticate(optional = true, build = build)
}

fun Route.authenticated(build: Route.(Auth) -> Unit) {
    authenticate {
        build(Auth())
    }
}

class Auth internal constructor() {

    class Wrapper<REQ : Any, RES : Any>(private val usecase: CommandUsecase<REQ, RES>) :
        CommandUsecase.Public<REQ, RES> {
        override suspend fun invoke(user: Usecase.User?, request: REQ): RES {
            return usecase.invoke(requireNotNull(user), request)
        }
    }

    operator fun <REQ : Any, RES : Any> invoke(usecase: CommandUsecase<REQ, RES>) = Wrapper(usecase)

    operator fun invoke(user: Usecase.User?) = requireNotNull(user)
}

fun ApplicationCall.user(): Usecase.User? {
    val token = principal<FirebasePrincipal>()?.token
    return token?.let {
        Usecase.User(StringId(token.uid), token.name)
    }
}

fun <U, RES : Any> Route.getWith(
    path: String, usecase: U,
    body: suspend PipelineContext<*, ApplicationCall>.(U) -> RES
) {
    get(path) {
        call.respond(body(usecase))
    }
}

inline fun <reified REQ : Any> Route.postWith(path: String, usecase: CommandUsecase.Public<REQ, *>) {
    post(path) {
        call.respond(usecase.invoke(call.user(), call.receive()))
    }
}
