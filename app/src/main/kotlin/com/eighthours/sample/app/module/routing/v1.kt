package com.eighthours.sample.app.module.routing

import com.eighthours.sample.usecase.user.v1.TestUsecase
import io.ktor.routing.*

fun Route.v1() = route("/v1") {
    postWith("/test", TestUsecase())
}
