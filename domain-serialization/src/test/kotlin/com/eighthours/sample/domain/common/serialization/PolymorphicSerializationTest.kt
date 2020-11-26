package com.eighthours.sample.domain.common.serialization

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PolymorphicSerializationTest {

    @Serializable
    sealed class Event {
        abstract val id: String

        @Serializable
        @SerialName("NAMED")
        data class Named(override val id: String, val name: String) : Event()

        @Serializable
        @SerialName("COUNTED")
        data class Counted(override val id: String, val count: Int) : Event()
    }

    @Test
    fun test() {
        val event: Event = Event.Named("a", "alice")
        assertThat(Json.encodeToString(event))
            .isEqualTo("""{"type":"NAMED","id":"a","name":"alice"}""")

        val decoded: Event = Json.decodeFromString("""{"type":"COUNTED","id":"b","count":1}""")
        assertThat(decoded).isEqualTo(Event.Counted("b", 1))
    }
}
