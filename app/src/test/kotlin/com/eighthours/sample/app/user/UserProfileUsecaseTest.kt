package com.eighthours.sample.app.user

import com.eighthours.sample.app.*
import com.eighthours.sample.usecase.user.v1.GetUserProfileUsecase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

class UserProfileUsecaseTest : KoinTest {

    @Test
    fun test() = testUsecase {
        withPost("/v1/user/profile", """{ "name": "TesterName" }""", Token.Tester) {
            response.ok()
        }
        withGet("/v1/users/${Token.Tester.id}/profile") {
            with(response.ok().content<GetUserProfileUsecase.Response>()) {
                assertThat(id.value).isEqualTo(Token.Tester.id)
                assertThat(name).isEqualTo("TesterName")
            }
        }
    }
}
