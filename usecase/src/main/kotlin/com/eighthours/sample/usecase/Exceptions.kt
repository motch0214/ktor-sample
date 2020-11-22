package com.eighthours.sample.usecase

abstract class UsecaseException(
    override val message: String, cause: Throwable? = null
) : Exception(message, cause)

class BadRequestException(
    override val message: String, cause: Throwable? = null
) : UsecaseException(message, cause)
