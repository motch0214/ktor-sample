package com.eighthours.sample.domain.common.doma

import com.eighthours.sample.domain.user.UserAttributes
import org.seasar.doma.DomainConverters
import org.seasar.doma.ExternalDomain

@DomainConverters(
    StringIdConverter::class,
    LongIdConverter::class,
    OffsetDateTimeConverter::class,
    UserAttributesConverter::class,
)
internal class DomainConvertersProvider

@ExternalDomain
internal class UserAttributesConverter : JsonColumnConverter<UserAttributes>(UserAttributes.serializer())
