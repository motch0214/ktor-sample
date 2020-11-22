package com.eighthours.sample.app.module

import com.eighthours.sample.support.module.AdditionalCallLogging
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.serialization.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import org.slf4j.event.Level
import java.util.*

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

    val suppressCallLogPath = setOf("/health")

    install(CallLogging) {
        level = Level.INFO
        callIdMdc()
        filter { call ->
            call.request.path() !in suppressCallLogPath
        }
    }
    install(AdditionalCallLogging) {
        filter { call ->
            call.request.path() !in suppressCallLogPath
        }
    }

    install(CallId) {
        val key = "call_id"
        reply { call, callId ->
            call.response.header(HttpHeaders.SetCookie, "$key=$callId")
        }
        retrieve { call ->
            call.request.cookies[key]
        }
        generate {
            UUID.randomUUID().toString().substringBefore("-")
        }
    }
}

@Serializable
data class BadRequestResponse(
    val type: String,
    val message: String?
)
