package com.eighthours.sample.domain.common

import org.koin.core.KoinComponent
import org.koin.core.get

interface TransactionSupport {

    suspend fun <T> required(block: Context.() -> T): T

    interface Context : KoinComponent
}

val KoinComponent.tx: TransactionSupport get() = get()

inline fun <reified T> TransactionSupport.Context.dao(): T {
    return get()
}
