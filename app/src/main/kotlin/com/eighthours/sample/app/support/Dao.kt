package com.eighthours.sample.app.support

import org.koin.core.scope.Scope
import org.seasar.doma.jdbc.Config

inline fun <reified DAO> Scope.dao(): DAO {
    val daoClass = DAO::class
    val name = "${daoClass.java.name}Impl"
    return daoClass.java.classLoader.loadClass(name)
        .getConstructor(Config::class.java).newInstance(get<Config>()) as DAO
}
