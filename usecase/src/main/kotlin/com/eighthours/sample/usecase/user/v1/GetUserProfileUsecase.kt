package com.eighthours.sample.usecase.user.v1

import com.eighthours.sample.domain.common.StringId
import com.eighthours.sample.domain.common.dao
import com.eighthours.sample.domain.common.serialization.OffsetDateTimeSerializer
import com.eighthours.sample.domain.common.tx
import com.eighthours.sample.domain.user.User
import com.eighthours.sample.domain.user.dao.UserProfileQueryDao
import com.eighthours.sample.usecase.NotFoundException
import com.eighthours.sample.usecase.Usecase
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

class GetUserProfileUsecase : Usecase {

    @Serializable
    data class Response(
        val id: StringId<User>,
        val name: String,
        @Serializable(with = OffsetDateTimeSerializer::class)
        val updated: OffsetDateTime,
        val version: Int,
    )

    suspend fun invoke(id: StringId<User>): Response {
        val profile = tx.required(query = true) {
            dao<UserProfileQueryDao>().select(id)
                ?: throw NotFoundException("UserProfile not found: userId=$id")
        }

        return Response(
            id = profile.userId,
            name = profile.name,
            updated = profile.updated,
            version = profile.version,
        )
    }
}
