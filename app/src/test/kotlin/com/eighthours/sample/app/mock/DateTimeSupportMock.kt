package com.eighthours.sample.app.mock

import com.eighthours.sample.domain.common.DateTimeSupport
import java.time.OffsetDateTime

class DateTimeSupportMock private constructor(private val time: OffsetDateTime) : DateTimeSupport {

    companion object {
        fun at(time: OffsetDateTime): DateTimeSupport {
            return DateTimeSupportMock(time)
        }
    }

    override fun now(): OffsetDateTime = time
}
