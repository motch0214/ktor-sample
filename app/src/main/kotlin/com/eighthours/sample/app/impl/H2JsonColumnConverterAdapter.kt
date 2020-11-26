package com.eighthours.sample.app.impl

import com.eighthours.sample.domain.common.doma.JsonColumnConverter
import java.lang.reflect.Method

class H2JsonColumnConverterAdapter : JsonColumnConverter.Adapter {

    private val fromJson: Method

    private val getString: Method

    init {
        val h2ValueJson: Class<*> = Class.forName("org.h2.value.ValueJson")
        fromJson = h2ValueJson.getDeclaredMethod("fromJson", String::class.java)
        getString = h2ValueJson.getDeclaredMethod("getString")
    }

    override fun fromJsonToValue(json: String): Any {
        return fromJson(null, json)
    }

    override fun fromValueToJson(value: Any): String {
        return if (value is ByteArray) {
            String(value)
        } else {
            getString(value) as String
        }
    }
}
