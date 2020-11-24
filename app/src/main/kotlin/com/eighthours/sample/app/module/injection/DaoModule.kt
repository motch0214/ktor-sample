package com.eighthours.sample.app.module.injection

import com.eighthours.sample.app.support.dao
import com.eighthours.sample.domain.user.dao.UserProfileDao
import com.eighthours.sample.domain.user.dao.UserProfileQueryDao
import org.koin.dsl.module

val DaoModule = module {
    single { dao<UserProfileQueryDao>() }
    single { dao<UserProfileDao>() }
}
