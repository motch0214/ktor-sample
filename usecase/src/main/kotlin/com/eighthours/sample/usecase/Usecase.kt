package com.eighthours.sample.usecase

import com.eighthours.sample.domain.common.StringId
import org.koin.core.KoinComponent
import com.eighthours.sample.domain.user.User as DomainUser

interface Usecase : KoinComponent {

    fun empty(): Empty = ""

    data class User(override val id: StringId<DomainUser>, val name: String?) : DomainUser
}

interface CommandUsecase<REQ : Any, RES : Any> : Usecase {

    suspend fun invoke(user: Usecase.User, request: REQ): RES

    interface Public<REQ : Any, RES : Any> : Usecase {

        suspend fun invoke(user: Usecase.User?, request: REQ): RES
    }
}

typealias Empty = String
