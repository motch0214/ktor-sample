package com.eighthours.sample.app.module

import com.eighthours.sample.app.module.injection.SystemModule
import io.ktor.application.*
import org.koin.core.logger.Level
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

fun Application.installInjection() {
    install(Koin) {
        slf4jLogger(Level.ERROR)
        modules(
            SystemModule,
        )
    }
}
