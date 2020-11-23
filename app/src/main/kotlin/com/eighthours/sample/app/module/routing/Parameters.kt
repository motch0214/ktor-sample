package com.eighthours.sample.app.module.routing

import com.eighthours.sample.usecase.BadRequestException
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.util.pipeline.*

val PipelineContext<*, ApplicationCall>.params: Parameters get() = call.parameters

val PipelineContext<*, ApplicationCall>.query: Parameters get() = call.request.queryParameters

fun Parameters.long(name: String): Long {
    val string = get(name)
        ?: throw BadRequestException("Parameter not found: $name")
    return string.toLongOrNull()
        ?: throw BadRequestException("Not a number for $name: $string")
}
