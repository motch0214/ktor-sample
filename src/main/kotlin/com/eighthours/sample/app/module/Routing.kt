package com.eighthours.sample.app.module

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.installRouting() = routing {
    get("/health") {
        call.respondText("Healthy")
    }
}
