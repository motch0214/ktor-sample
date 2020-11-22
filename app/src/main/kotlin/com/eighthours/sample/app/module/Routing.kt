package com.eighthours.sample.app.module

import com.eighthours.sample.domain.common.now
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.koin.core.KoinComponent
import java.time.OffsetDateTime

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

class TestController : KoinComponent {

    @Serializable
    data class Request(
        val id: Long,
    )

    @Serializable
    data class Response(
        val message: String,
        @Contextual
        val dateTime: OffsetDateTime,
    )

    fun invoke(request: Request): Response {
        return Response(
            message = "Hello, ${request.id}",
            dateTime = now()
        )
    }
}
