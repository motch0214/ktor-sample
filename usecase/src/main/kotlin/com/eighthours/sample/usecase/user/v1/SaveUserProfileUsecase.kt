package com.eighthours.sample.usecase.user.v1

import com.eighthours.sample.domain.common.dao
import com.eighthours.sample.domain.common.now
import com.eighthours.sample.domain.common.tx
import com.eighthours.sample.domain.user.UserAttributes
import com.eighthours.sample.domain.user.UserProfile
import com.eighthours.sample.domain.user.dao.UserProfileDao
import com.eighthours.sample.usecase.CommandUsecase
import com.eighthours.sample.usecase.Empty
import com.eighthours.sample.usecase.UpdateConflictException
import com.eighthours.sample.usecase.Usecase
import kotlinx.serialization.Serializable
import org.seasar.doma.jdbc.OptimisticLockException
import org.seasar.doma.jdbc.UniqueConstraintException

class SaveUserProfileUsecase : CommandUsecase<SaveUserProfileUsecase.Request, Empty> {

    @Serializable
    data class Request(
        val name: String,
        val attributes: UserAttributes,
        val version: Int? = null,
    )

    override suspend fun invoke(user: Usecase.User, request: Request): Empty {
        val profile = UserProfile(
            userId = user.id,
            name = request.name,
            attributes = request.attributes,
            updated = now(),
            version = request.version ?: -1
        )

        tx.required {
            if (request.version == null) {
                try {
                    dao<UserProfileDao>().insert(profile)
                } catch (e: UniqueConstraintException) {
                    throw UpdateConflictException(e)
                }
            } else {
                try {
                    dao<UserProfileDao>().update(profile)
                } catch (e: OptimisticLockException) {
                    throw UpdateConflictException(e)
                }
            }
        }

        return empty()
    }
}
