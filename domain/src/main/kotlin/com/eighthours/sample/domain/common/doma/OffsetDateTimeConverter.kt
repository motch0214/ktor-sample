package com.eighthours.sample.domain.common.doma

import com.eighthours.sample.domain.common.now
import org.koin.core.KoinComponent
import org.seasar.doma.ExternalDomain
import org.seasar.doma.jdbc.domain.DomainConverter
import java.time.OffsetDateTime

@ExternalDomain
internal class OffsetDateTimeConverter : DomainConverter<OffsetDateTime, java.util.Date>, KoinComponent {

    override fun fromDomainToValue(domain: OffsetDateTime?): java.util.Date? {
        return domain?.let {
            java.util.Date.from(domain.toInstant())
        }
    }

    override fun fromValueToDomain(value: java.util.Date?): OffsetDateTime? {
        return value?.toInstant()?.let {
            OffsetDateTime.ofInstant(it, now().offset)
        }
    }
}
