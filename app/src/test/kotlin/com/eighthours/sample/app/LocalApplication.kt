package com.eighthours.sample.app

import com.eighthours.sample.app.module.Injector
import com.eighthours.sample.app.module.Modules
import io.ktor.application.*
import org.koin.core.logger.Level
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

fun main() {
    startWith(TestInjector())
}

class TestInjector : Injector {
    override fun Application.installInjection() {
        install(Koin) {
            slf4jLogger(Level.ERROR)
            modules(Modules)
        }
    }
}
