package com.eighthours.sample.domain.common.doma

import com.eighthours.sample.domain.common.LongId
import com.eighthours.sample.domain.common.StringId
import org.koin.core.KoinComponent
import org.seasar.doma.ExternalDomain
import org.seasar.doma.jdbc.domain.DomainConverter

@ExternalDomain
internal class StringIdConverter : DomainConverter<StringId<*>, String>, KoinComponent {

    override fun fromDomainToValue(domain: StringId<*>?): String? {
        return domain?.value
    }

    override fun fromValueToDomain(value: String?): StringId<*>? {
        return value?.let { StringId<Any>(it) }
    }
}


@ExternalDomain
internal class LongIdConverter : DomainConverter<LongId<*>, Long>, KoinComponent {

    override fun fromDomainToValue(domain: LongId<*>?): Long? {
        return domain?.value
    }

    override fun fromValueToDomain(value: Long?): LongId<*>? {
        return value?.let { LongId<Any>(it) }
    }
}
