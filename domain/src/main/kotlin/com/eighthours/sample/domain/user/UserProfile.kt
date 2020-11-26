package com.eighthours.sample.domain.user

import com.eighthours.sample.domain.common.StringId
import org.seasar.doma.Entity
import org.seasar.doma.Id
import org.seasar.doma.Version
import java.time.OffsetDateTime

@Entity(immutable = true)
data class UserProfile(
    @Id
    val userId: StringId<User>,

    val name: String,

    val attributes: UserAttributes,

    val updated: OffsetDateTime,

    @Version
    val version: Int = -1,
)
