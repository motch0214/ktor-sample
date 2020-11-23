package com.eighthours.sample.app.module.routing

import com.eighthours.sample.usecase.user.v1.TestUsecase
import io.ktor.auth.*
import io.ktor.routing.*

fun Route.v1() = route("/v1") {
    authenticate {
        postWith("/test", TestUsecase())
    }
}
