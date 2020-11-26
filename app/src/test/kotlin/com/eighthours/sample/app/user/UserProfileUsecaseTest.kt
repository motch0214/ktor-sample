package com.eighthours.sample.app.user

import com.eighthours.sample.app.*
import com.eighthours.sample.app.mock.Tester
import com.eighthours.sample.domain.user.UserAttributes
import com.eighthours.sample.usecase.user.v1.GetUserProfileUsecase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import java.time.OffsetDateTime

class UserProfileUsecaseTest : KoinTest {

    private val time = OffsetDateTime.parse("2021-01-01T00:00+09:00")

    @Test
    fun test() = testUsecase(at = time) {
        withPost("/v1/user/profile", """{ "name": "TesterName", "attributes": {} }""", Tester.token) {
            response.ok()
        }

        withGet("/v1/users/${Tester.id}/profile") {
            with(response.ok().content<GetUserProfileUsecase.Response>()) {
                assertThat(id).isEqualTo(Tester.id)
                assertThat(name).isEqualTo("TesterName")
                assertThat(attributes).isEqualTo(UserAttributes())
                assertThat(updated).isEqualTo(time)
            }
        }

        at(time + 1.hours)
        withPost("/v1/user/profile", """{ "name": "TN", "attributes": { "gender": "FEMALE" }, "version": 1 }""", Tester.token) {
            response.ok()
        }

        withGet("/v1/users/${Tester.id}/profile") {
            with(response.ok().content<GetUserProfileUsecase.Response>()) {
                assertThat(id).isEqualTo(Tester.id)
                assertThat(name).isEqualTo("TN")
                assertThat(attributes).isEqualTo(UserAttributes(gender = UserAttributes.Gender.FEMALE))
                assertThat(updated).isEqualTo(time + 1.hours)
            }
        }
    }
}
