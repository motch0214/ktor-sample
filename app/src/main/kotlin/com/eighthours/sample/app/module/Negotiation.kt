package com.eighthours.sample.app.module

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException

fun Application.installNegotiation() {
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<SerializationException> { cause ->
            log.debug("${cause.javaClass.simpleName}: ${call.request.toLogString()} - ${cause.message}")
            call.respond(
                HttpStatusCode.BadRequest,
                BadRequestResponse(type = "SerializationException", message = cause.message)
            )
        }
    }
}

@Serializable
data class BadRequestResponse(
    val type: String,
    val message: String?
)
