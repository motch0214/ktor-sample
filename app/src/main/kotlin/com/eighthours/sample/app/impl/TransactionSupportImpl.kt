package com.eighthours.sample.app.impl

import com.eighthours.sample.domain.common.TransactionSupport
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.seasar.doma.jdbc.Config
import java.util.function.Supplier

class TransactionSupportImpl : TransactionSupport, KoinComponent {

    private val config: Config by inject()

    override suspend fun <T> required(block: TransactionSupport.Context.() -> T): T {
        return withContext(Dispatchers.IO) {
            config.transactionManager.required(Supplier { Context().block() })
        }
    }

    private class Context : TransactionSupport.Context
}
