package com.eighthours.sample.usecase.user.v1

import com.eighthours.sample.domain.common.StringId
import com.eighthours.sample.domain.user.User
import com.eighthours.sample.usecase.Usecase
import kotlinx.serialization.Serializable

class GetMyUserAccountUsecase : Usecase {

    @Serializable
    data class Response(
        val id: StringId<User>
    )

    suspend fun invoke(user: Usecase.User): Response {
        // TODO
        return Response(
            id = user.id,
        )
    }
}
