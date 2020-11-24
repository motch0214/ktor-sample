package com.eighthours.sample.domain.common.doma

import org.seasar.doma.DomainConverters

@DomainConverters(
    StringIdConverter::class,
    LongIdConverter::class,
    OffsetDateTimeConverter::class,
)
internal class DomainConvertersProvider
