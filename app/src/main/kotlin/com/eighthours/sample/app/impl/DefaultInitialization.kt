package com.eighthours.sample.app.impl

import com.eighthours.sample.app.support.Initialization
import com.eighthours.sample.domain.common.tx
import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.seasar.doma.jdbc.Config

open class DefaultInitialization : Initialization, KoinComponent {

    private val config: Config by inject()

    init {
        val placeholders = ConfigFactory.load().getConfig("database.flyway").entrySet()
            .map { (key, value) ->
                key to value.unwrapped() as String
            }
            .toMap()

        val flyway = Flyway.configure()
            .dataSource(config.dataSource)
            .placeholders(placeholders)
            .load()

        runBlocking {
            execute(flyway)
        }
    }

    open suspend fun execute(flyway: Flyway) {
        tx.required {
            flyway.migrate()
        }
    }
}
