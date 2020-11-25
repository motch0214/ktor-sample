package com.eighthours.sample.app.impl

import com.eighthours.sample.domain.common.DateTimeSupport
import com.typesafe.config.ConfigFactory
import java.time.OffsetDateTime
import java.time.ZoneOffset

class DateTimeSupportImpl : DateTimeSupport {

    private val timezone: ZoneOffset = ZoneOffset.of(ConfigFactory.load().getString("system.timezone"))

    override fun now(): OffsetDateTime = OffsetDateTime.now(timezone)
}
