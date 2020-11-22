package com.eighthours.sample.app.module

import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*

fun Application.installSecurity() {
    val config = ConfigFactory.load().getConfig("ktor")

    val adminPort = config.getInt("admin.port")
    intercept(ApplicationCallPipeline.Features) {
        if (call.request.path().startsWith("/admin") && call.request.port() != adminPort) {
            // Error because of calling Private API from NOT local
            return@intercept finish()
        }
    }

    if (config.hasPath("cors.hosts")) {
        val schemes = config.getString("cors.schemes")
            .split(",").map { it.trim() }
        val hosts = config.getString("cors.hosts")
            .split(",").map { it.trim() }
        val maxAgeSeconds = config.getLong("cors.max-age-seconds")

        install(CORS) {
            hosts.forEach { host(it, schemes) }
            allowNonSimpleContentTypes = true
            header(HttpHeaders.Authorization)
            maxAgeInSeconds = maxAgeSeconds
        }
    }
}
