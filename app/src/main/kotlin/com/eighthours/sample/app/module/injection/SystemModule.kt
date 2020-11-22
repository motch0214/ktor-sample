package com.eighthours.sample.app.module.injection

import com.eighthours.sample.app.module.serialization.OffsetDateTimeSerializer
import com.eighthours.sample.domain.common.DateTimeSupport
import com.typesafe.config.ConfigFactory
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.koin.dsl.module
import java.time.OffsetDateTime
import java.time.ZoneId

val SystemModule = module {
    single {
        SerializersModule {
            contextual(OffsetDateTimeSerializer)
        }
    }

    single<DateTimeSupport> { DateTimeSupportImpl() }
}

private class DateTimeSupportImpl : DateTimeSupport {

    val timezone: ZoneId = ZoneId.of(ConfigFactory.load().getString("system.zone"))

    override fun now(): OffsetDateTime = OffsetDateTime.now(timezone)

    override fun timezone(): ZoneId = timezone
}
