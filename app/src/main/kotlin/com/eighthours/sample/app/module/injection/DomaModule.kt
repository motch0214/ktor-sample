package com.eighthours.sample.app.module.injection

import com.eighthours.sample.app.impl.DomaConfig
import com.eighthours.sample.app.impl.TransactionSupportImpl
import com.eighthours.sample.domain.common.TransactionSupport
import com.typesafe.config.ConfigFactory
import com.zaxxer.hikari.HikariConfig
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.ext.getOrCreateScope
import org.koin.ext.getScopeId
import org.seasar.doma.jdbc.Config
import org.seasar.doma.jdbc.dialect.Dialect
import java.util.*

object QueryScope : TransactionSupport.Scope {
    override val id: String = getScopeId()
}

object CommandScope : TransactionSupport.Scope {
    override val id: String = getScopeId()
}

val DomaModule = module {
    single(createdAtStart = true) {
        CommandScope.getOrCreateScope().also { it.linkTo(QueryScope.getOrCreateScope()) }
    }
    single(named(QueryScope.id), createdAtStart = true) {
        if (System.getenv(DATABASE_URL) == null && System.getenv(DATABASE_QUERY_HOST) != null) {
            initDomaConfig(query = true)
        } else {
            get(named(CommandScope.id))
        }
    }
    single(named(CommandScope.id), createdAtStart = true) {
        initDomaConfig(query = false)
    }

    single<TransactionSupport> { TransactionSupportImpl() }
}

private const val DATABASE_URL = "DATABASE_URL"
private const val DATABASE_QUERY_HOST = "DATABASE_QUERY_HOST"
private const val DATABASE_SECRET_JSON = "DATABASE_SECRET_JSON"

@Serializable
data class DatabaseSecret(
    // DATABASE_URL を設定しないときはすべて必須。
    val host: String? = null,
    val port: Int? = null,
    val dbname: String? = null,
    val username: String,
    val password: String
)

private fun initDomaConfig(query: Boolean): Config {
    val config = ConfigFactory.load().getConfig("database")

    val dialect = Class.forName(config.getString("dialect"))
        .getConstructor().newInstance() as Dialect

    val secret = System.getenv(DATABASE_SECRET_JSON)?.let {
        Json.decodeFromString<DatabaseSecret>(it)
    } ?: error("$DATABASE_SECRET_JSON not set.")

    val url = System.getenv(DATABASE_URL) ?: run {
        if (secret.host == null || secret.port == null || secret.dbname == null) {
            error("$DATABASE_SECRET_JSON not enough.")
        }

        val scheme = config.getString("scheme")
        val host = if (query) System.getenv(DATABASE_QUERY_HOST) else secret.host
        val options = config.getConfig("options").entrySet()
            .joinToString("&") { (key, value) ->
                "$key=${value.unwrapped()}"
            }.let {
                if (it.isEmpty()) "" else "?$it"
            }

        "$scheme://$host:${secret.port}/${secret.dbname}$options"
    }

    val hikariConfig = HikariConfig(Properties().apply {
        val pool = if (query) "query" else "command"
        config.getConfig("hikari.$pool").entrySet().forEach { (key, value) ->
            set(key, value.unwrapped())
        }
    }).apply {
        poolName = if (query) "QueryPool" else "CommandPool"
        jdbcUrl = url
        username = secret.username
        password = secret.password
    }

    return DomaConfig(hikariConfig, dialect)
}
