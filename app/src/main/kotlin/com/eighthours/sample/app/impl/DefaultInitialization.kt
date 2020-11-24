package com.eighthours.sample.app.impl

import com.eighthours.sample.app.support.Initialization
import com.eighthours.sample.domain.common.tx
import com.typesafe.config.ConfigFactory
import kotlinx.coroutines.runBlocking
import org.flywaydb.core.Flyway
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.seasar.doma.jdbc.Config

class DefaultInitialization : Initialization {

    companion object : KoinComponent {

        private val config: Config by inject()

        fun createFlyway(): Flyway {
            val placeholders = ConfigFactory.load().getConfig("database.flyway").entrySet()
                .map { (key, value) ->
                    key to value.unwrapped() as String
                }
                .toMap()

            return Flyway.configure()
                .dataSource(config.dataSource)
                .placeholders(placeholders)
                .load()
        }
    }

    init {
        val flyway = createFlyway()
        runBlocking {
            tx.required {
                flyway.migrate()
            }
        }
    }
}
