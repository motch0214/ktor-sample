package com.eighthours.sample.app.module

import com.eighthours.sample.app.module.injection.DaoModule
import com.eighthours.sample.app.module.injection.DomaModule
import com.eighthours.sample.app.module.injection.FirebaseModule
import com.eighthours.sample.app.module.injection.SystemModule
import io.ktor.application.*
import org.koin.core.logger.Level
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

val Modules = listOf(
    SystemModule,
    DomaModule,
    DaoModule,
    FirebaseModule,
)

interface Injector {
    fun Application.installInjection()
}

class DefaultInjector : Injector {
    override fun Application.installInjection() {
        install(Koin) {
            slf4jLogger(Level.ERROR)
            modules(Modules)
        }
    }
}
