package com.eighthours.sample.usecase

import org.koin.core.KoinComponent

interface Usecase : KoinComponent {

    fun empty(): Empty = ""

    data class User(val id: String, val name: String?)
}

interface CommandUsecase<REQ : Any, RES : Any> : Usecase {

    suspend fun invoke(user: Usecase.User, request: REQ): RES

    interface Public<REQ : Any, RES : Any> : Usecase {

        suspend fun invoke(user: Usecase.User?, request: REQ): RES
    }
}

typealias Empty = String
