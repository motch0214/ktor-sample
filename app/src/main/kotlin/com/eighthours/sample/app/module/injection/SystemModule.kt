package com.eighthours.sample.app.module.injection

import com.eighthours.sample.app.impl.DateTimeSupportImpl
import com.eighthours.sample.app.impl.DefaultInitialization
import com.eighthours.sample.app.support.Initialization
import com.eighthours.sample.domain.common.DateTimeSupport
import org.koin.dsl.module

val SystemModule = module {
    single<Initialization>(createdAtStart = true) { DefaultInitialization() }
    single<DateTimeSupport> { DateTimeSupportImpl() }
}
