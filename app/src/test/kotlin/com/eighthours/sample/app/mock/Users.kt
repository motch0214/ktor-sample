package com.eighthours.sample.app.mock

import com.eighthours.sample.app.Token
import com.eighthours.sample.domain.common.StringId
import com.eighthours.sample.domain.user.User

object Tester {
    val id = StringId<User>("tester")
    val token = Token(id, "Tester")
}
