package com.eighthours.sample.app.support

import com.eighthours.sample.domain.common.TransactionSupport
import org.koin.core.context.KoinContextHandler
import org.koin.core.definition.BeanDefinition
import org.koin.core.qualifier.TypeQualifier
import org.koin.dsl.ScopeDSL
import org.seasar.doma.jdbc.Config

inline fun <reified DAO> dao(config: Config): DAO {
    val daoClass = DAO::class
    val name = "${daoClass.java.name}Impl"
    return daoClass.java.classLoader.loadClass(name)
        .getConstructor(Config::class.java).newInstance(config) as DAO
}

inline fun <reified T> ScopeDSL.defineDao(): BeanDefinition<T> {
    return factory { (scope: TransactionSupport.Scope) ->
        dao(scope.getConfig())
    }
}

fun TransactionSupport.Scope.getConfig(): Config {
    return KoinContextHandler.get().get(TypeQualifier(this::class))
}
