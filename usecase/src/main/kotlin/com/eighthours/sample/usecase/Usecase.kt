package com.eighthours.sample.usecase

import org.koin.core.KoinComponent

interface Usecase : KoinComponent {

    fun empty(): Empty = ""
}

interface PostUsecase<REQ : Any, RES : Any> : Usecase {

    suspend fun invoke(request: REQ): RES
}

typealias Empty = String
