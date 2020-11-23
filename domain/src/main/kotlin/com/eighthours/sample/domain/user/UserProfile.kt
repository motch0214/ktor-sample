package com.eighthours.sample.domain.user

import com.eighthours.sample.domain.common.StringId

data class UserProfile(
    val id: StringId<User>,
)
