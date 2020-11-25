package com.eighthours.sample.domain.common

import com.eighthours.sample.domain.common.serialization.LongIdSerializer
import com.eighthours.sample.domain.common.serialization.StringIdSerializer
import kotlinx.serialization.Serializable

// Workaround for https://github.com/Kotlin/kotlinx.serialization/issues/685

@Serializable(with = StringIdSerializer::class)
data class StringId<E : Any>(val value: String) {

    override fun toString(): String = value
}

@Serializable(with = LongIdSerializer::class)
data class LongId<E : Any>(val value: Long) {

    override fun toString(): String = value.toString()
}
