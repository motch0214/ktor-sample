package com.eighthours.sample.app.module.routing

import com.eighthours.sample.domain.common.LongId
import com.eighthours.sample.domain.common.StringId
import com.eighthours.sample.usecase.BadRequestException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.util.pipeline.*

val PipelineContext<*, ApplicationCall>.params: Parameters get() = call.parameters

val PipelineContext<*, ApplicationCall>.query: Parameters get() = call.request.queryParameters

fun <E : Any> Parameters.stringId(name: String): StringId<E> {
    val string = get(name)
        ?: throw BadRequestException("Parameter not found: $name")
    if (string.isBlank()) {
        throw BadRequestException("Blank: $name")
    }
    return StringId(string)
}

fun <E : Any> Parameters.longId(name: String): LongId<E> {
    return LongId(long(name))
}

fun Parameters.long(name: String): Long {
    val string = get(name)
        ?: throw BadRequestException("Parameter not found: $name")
    return string.toLongOrNull()
        ?: throw BadRequestException("Not a number for $name: $string")
}
