package com.eighthours.sample.usecase.user.v1

import com.eighthours.sample.domain.common.now
import com.eighthours.sample.usecase.PostUsecase
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

class TestUsecase : PostUsecase<TestUsecase.Request, TestUsecase.Response> {

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

    override suspend fun invoke(request: Request): Response {
        return Response(
            message = "Hello, ${request.id}",
            dateTime = now()
        )
    }
}
