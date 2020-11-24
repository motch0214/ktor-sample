package com.eighthours.sample.app.module

import com.eighthours.sample.app.module.routing.getWith
import com.eighthours.sample.app.module.routing.v1
import com.eighthours.sample.usecase.system.HealthCheckUsecase
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.server.engine.*

fun Application.installRouting() {
    routing {
        v1()

        getWith("/health", HealthCheckUsecase()) {
            it.invoke()
        }
    }

    install(ShutDownUrl.ApplicationCallFeature) {
        shutDownUrl = "/admin/shutdown"
    }
}
