package com.eighthours.sample.app.impl

import com.eighthours.sample.app.support.Slf4jJdbcLogger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.seasar.doma.jdbc.JdbcLogger
import org.seasar.doma.jdbc.Naming
import org.seasar.doma.jdbc.dialect.Dialect
import org.seasar.doma.jdbc.tx.LocalTransactionDataSource
import org.seasar.doma.jdbc.tx.LocalTransactionManager
import org.seasar.doma.jdbc.tx.TransactionManager
import java.io.Closeable
import javax.sql.DataSource

class DomaConfig(config: HikariConfig, private val dialect: Dialect) : org.seasar.doma.jdbc.Config, Closeable {

    private val hikari = HikariDataSource(config)

    private val dataSource = LocalTransactionDataSource(hikari)

    private val logger = Slf4jJdbcLogger()

    private val transactionManager = LocalTransactionManager(dataSource.getLocalTransaction(jdbcLogger))

    override fun getDataSource(): DataSource = dataSource

    override fun getDialect(): Dialect = dialect

    override fun getTransactionManager(): TransactionManager = transactionManager

    override fun getNaming(): Naming = Naming.SNAKE_LOWER_CASE

    override fun getJdbcLogger(): JdbcLogger = logger

    override fun close() {
        hikari.close()
    }
}
