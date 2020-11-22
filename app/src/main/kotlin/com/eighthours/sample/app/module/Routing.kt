package com.eighthours.sample.app.module

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Serializable

fun Application.installRouting() {
    routing {
        get("/health") {
            call.respondText("Healthy")
        }

        post("/test") {
            val response = TestController().invoke(call.receive())
            call.respond(response)
        }
    }
}

class TestController {

    @Serializable
    data class Request(
        val id: Long
    )

    @Serializable
    data class Response(
        val message: String
    )

    fun invoke(request: Request): Response {
        return Response(
            message = "Hello, ${request.id}"
        )
    }
}
