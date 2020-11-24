package com.eighthours.sample.app.impl

import com.eighthours.sample.domain.common.DateTimeSupport
import com.typesafe.config.ConfigFactory
import java.time.OffsetDateTime
import java.time.ZoneId

class DateTimeSupportImpl : DateTimeSupport {

    val timezone: ZoneId = ZoneId.of(ConfigFactory.load().getString("system.timezone"))

    override fun now(): OffsetDateTime = OffsetDateTime.now(timezone)

    override fun timezone(): ZoneId = timezone
}
