package com.eighthours.sample.app.support

import org.seasar.doma.jdbc.AbstractJdbcLogger
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.util.function.Supplier

class Slf4jJdbcLogger(private val log: Logger = LoggerFactory.getLogger("Doma"), level: Level = Level.TRACE) :
    AbstractJdbcLogger<Level>(level) {

    override fun log(
        level: Level,
        callerClassName: String?,
        callerMethodName: String?,
        throwable: Throwable?,
        messageSupplier: Supplier<String>
    ) {
        when (level) {
            Level.ERROR -> if (log.isErrorEnabled) log.error(messageSupplier.get(), throwable)
            Level.WARN -> if (log.isWarnEnabled) log.warn(messageSupplier.get(), throwable)
            Level.INFO -> if (log.isInfoEnabled) log.info(messageSupplier.get(), throwable)
            Level.DEBUG -> if (log.isDebugEnabled) log.debug(messageSupplier.get(), throwable)
            Level.TRACE -> if (log.isTraceEnabled) log.trace(messageSupplier.get(), throwable)
        }
    }
}
