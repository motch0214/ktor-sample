package com.eighthours.sample.app.impl

import com.eighthours.sample.app.module.injection.CommandScope
import com.eighthours.sample.app.module.injection.QueryScope
import com.eighthours.sample.app.support.getConfig
import com.eighthours.sample.domain.common.TransactionSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import java.util.function.Supplier

class TransactionSupportImpl : TransactionSupport, KoinComponent {

    override suspend fun <T> required(query: Boolean, block: TransactionSupport.Context.() -> T): T {
        return withContext(Dispatchers.IO) {
            val scope = if (query) QueryScope else CommandScope
            val config = scope.getConfig()
            config.transactionManager.required(Supplier { Context(scope).block() })
        }
    }

    private class Context(override val scope: TransactionSupport.Scope) : TransactionSupport.Context
}
