package com.eighthours.sample.domain.user

import com.eighthours.sample.domain.common.StringId

interface User {
    val id: StringId<User>
}
