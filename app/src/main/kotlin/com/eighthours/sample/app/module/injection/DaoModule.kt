package com.eighthours.sample.app.module.injection

import com.eighthours.sample.app.support.defineDao
import com.eighthours.sample.domain.user.dao.UserProfileDao
import com.eighthours.sample.domain.user.dao.UserProfileQueryDao
import org.koin.dsl.module

val DaoModule = module {
    scope<QueryScope> {
        defineDao<UserProfileQueryDao>()
    }

    scope<CommandScope> {
        defineDao<UserProfileDao>()
    }
}
