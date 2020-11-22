package com.eighthours.sample.usecase.system

import com.eighthours.sample.usecase.Usecase

class HealthCheckUsecase : Usecase {

    suspend fun invoke(): String {
        // TODO
        return "Healthy"
    }
}
