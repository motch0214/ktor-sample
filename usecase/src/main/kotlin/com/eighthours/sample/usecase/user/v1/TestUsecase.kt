package com.eighthours.sample.usecase.user.v1

import com.eighthours.sample.domain.common.now
import com.eighthours.sample.usecase.CommandUsecase
import com.eighthours.sample.usecase.Usecase
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

class TestUsecase : CommandUsecase<TestUsecase.Request, TestUsecase.Response> {

    private val log = LoggerFactory.getLogger(this::class.java)

    @Serializable
    data class Request(
        val id: Long,
    )

    @Serializable
    data class Response(
        val message: String,
        @Contextual
        val dateTime: OffsetDateTime,
    )

    override suspend fun invoke(user: Usecase.User, request: Request): Response {
        log.debug("$user")
        return Response(
            message = "Hello, ${request.id}",
            dateTime = now()
        )
    }
}
