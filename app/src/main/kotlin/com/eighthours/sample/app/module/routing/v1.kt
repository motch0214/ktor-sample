package com.eighthours.sample.app.module.routing

import com.eighthours.sample.usecase.user.v1.GetMyUserAccountUsecase
import com.eighthours.sample.usecase.user.v1.GetUserProfileUsecase
import io.ktor.application.*
import io.ktor.routing.*

fun Route.v1() = route("/v1") {
    public {
        getWith("/users/{id}/profile", GetUserProfileUsecase()) {
            it.invoke(params.stringId("id"))
        }
    }
    authenticated { auth ->
        getWith("/account", GetMyUserAccountUsecase()) {
            it.invoke(auth(call.user()))
        }
    }
}
