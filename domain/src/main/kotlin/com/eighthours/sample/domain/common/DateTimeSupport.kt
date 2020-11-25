package com.eighthours.sample.domain.common

import org.koin.core.KoinComponent
import org.koin.core.get
import java.time.OffsetDateTime

interface DateTimeSupport {
    fun now(): OffsetDateTime
}

fun KoinComponent.now(): OffsetDateTime {
    return get<DateTimeSupport>().now()
}
