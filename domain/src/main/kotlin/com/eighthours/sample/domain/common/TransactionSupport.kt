package com.eighthours.sample.domain.common

import org.koin.core.KoinComponent
import org.koin.core.context.KoinContextHandler
import org.koin.core.get
import org.koin.core.parameter.parametersOf

interface TransactionSupport {

    suspend fun <T> required(query: Boolean = false, block: Context.() -> T): T

    interface Context {
        val scope: Scope
    }

    interface Scope {
        val id: String
    }
}

val KoinComponent.tx: TransactionSupport
    get() = get()

inline fun <reified T> TransactionSupport.Context.dao(): T {
    val scoped = KoinContextHandler.get().getScope(scope.id)
    return scoped.get { parametersOf(scope) }
}
