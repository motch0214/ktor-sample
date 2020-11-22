package com.eighthours.sample.app.module.routing

import com.eighthours.sample.usecase.PostUsecase
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.util.pipeline.*

fun <U, RES : Any> Route.getWith(
    path: String, usecase: U,
    body: suspend PipelineContext<*, ApplicationCall>.(U) -> RES
) {
    get(path) {
        call.respond(body(usecase))
    }
}

inline fun <reified REQ : Any> Route.postWith(path: String, usecase: PostUsecase<REQ, *>) {
    post(path) {
        call.respond(usecase.invoke(call.receive()))
    }
}
