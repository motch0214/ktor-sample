package com.eighthours.sample.app.module.routing

import com.eighthours.sample.usecase.user.v1.GetUserProfileUsecase
import com.eighthours.sample.usecase.user.v1.SaveUserProfileUsecase
import io.ktor.routing.*

fun Route.v1() = route("/v1") {
    public {
        getWith("/users/{id}/profile", GetUserProfileUsecase()) {
            it.invoke(params.stringId("id"))
        }
    }
    authenticated { auth ->
        postWith("/user/profile", auth(SaveUserProfileUsecase()))
    }
}
