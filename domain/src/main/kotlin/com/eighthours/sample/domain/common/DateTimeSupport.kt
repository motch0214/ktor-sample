package com.eighthours.sample.domain.common

import org.koin.core.KoinComponent
import org.koin.core.get
import java.time.OffsetDateTime
import java.time.ZoneId

interface DateTimeSupport {

    fun now(): OffsetDateTime

    fun timezone(): ZoneId
}

fun KoinComponent.now(): OffsetDateTime {
    return get<DateTimeSupport>().now()
}

fun KoinComponent.timezone(): ZoneId {
    return get<DateTimeSupport>().timezone()
}
