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
    return StringId(string(name))
}

fun Parameters.string(name: String, default: String? = null): String {
    return get(name) ?: if (default != null) {
        return default
    } else {
        throw BadRequestException("Parameter not found: $name")
    }
}

fun <E : Any> Parameters.longId(name: String): LongId<E> {
    return LongId(long(name))
}

fun Parameters.long(name: String, default: Long? = null): Long {
    val string = get(name) ?: if (default != null) {
        return default
    } else {
        throw BadRequestException("Parameter not found: $name")
    }
    return string.toLongOrNull()
        ?: throw BadRequestException("Not a number for $name: $string")
}
