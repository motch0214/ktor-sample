package com.eighthours.sample.domain.common.doma

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.seasar.doma.jdbc.domain.DomainConverter

abstract class JsonColumnConverter<T> internal constructor(private val serializer: KSerializer<T>) :
    DomainConverter<T, Any> {

    interface Adapter {
        fun fromJsonToValue(json: String): Any = json
        fun fromValueToJson(value: Any): String = value as String
    }

    companion object : KoinComponent {
        private val adapter: Adapter by inject()
    }

    override fun fromDomainToValue(domain: T?): Any? {
        return domain?.let { adapter.fromJsonToValue(Json.encodeToString(serializer, domain)) }
    }

    override fun fromValueToDomain(value: Any?): T? {
        return value?.let { Json.decodeFromString(serializer, adapter.fromValueToJson(value)) }
    }
}
