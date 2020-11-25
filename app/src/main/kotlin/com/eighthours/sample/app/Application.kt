package com.eighthours.sample.app

import com.eighthours.sample.app.module.*
import com.typesafe.config.ConfigFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.slf4j.LoggerFactory

fun main() {
    startWith(DefaultInjector())
}

fun startWith(injector: Injector) {
    setupExceptionHandler()

    val config = ConfigFactory.load().getConfig("ktor")
    val env = applicationEngineEnvironment {
        module {
            with(injector) { installInjection() }
            installNegotiation()
            installSecurity()
            installRouting()
        }

        connector {
            host = config.getString("host")
            port = config.getInt("port")
        }
        connector {
            host = config.getString("admin.host")
            port = config.getInt("admin.port")
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

private fun setupExceptionHandler() {
    val log = LoggerFactory.getLogger("ExceptionHandler")
    Thread.setDefaultUncaughtExceptionHandler { thread, e ->
        log.error("Uncaught exception in thread ${thread.name}", e)
    }
}
