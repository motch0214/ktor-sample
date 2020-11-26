package com.eighthours.sample.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class UserAttributes(
    val country: String? = null,
    val birthYear: Int? = null,
    val gender: Gender? = null,
) {

    enum class Gender {
        MALE, FEMALE, OTHER
    }
}
