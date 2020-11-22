package com.eighthours.sample.app

import com.eighthours.sample.app.module.installNegotiation
import com.eighthours.sample.app.module.installRouting
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

fun main() {
    val config = ConfigFactory.load().getConfig("ktor")
    val env = applicationEngineEnvironment {
        module {
            installNegotiation()
            installRouting()
            install(ShutDownUrl.ApplicationCallFeature) {
                shutDownUrl = "/admin/shutdown"
            }
        }

        connector {
            host = config.getString("host")
            port = config.getInt("port")
        }

        log = LoggerFactory.getLogger("SampleApplication")
    }

    embeddedServer(Netty, env) {
        requestQueueLimit = config.getInt("netty.requestQueueLimit")
        runningLimit = config.getInt("netty.runningLimit")
        responseWriteTimeoutSeconds = config.getInt("netty.responseWriteTimeoutSeconds")
        requestReadTimeoutSeconds = config.getInt("netty.requestReadTimeoutSeconds")
    }.start(true)
}
