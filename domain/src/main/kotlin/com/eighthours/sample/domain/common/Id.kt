package com.eighthours.sample.domain.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = StringId.Serializer::class)
data class StringId<E : Any>(val value: String) {

    @Suppress("UNUSED_PARAMETER")
    class Serializer<E : Any>(serializer: KSerializer<E>) : KSerializer<StringId<E>> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("StringId", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: StringId<E>) {
            encoder.encodeString(value.value)
        }

        override fun deserialize(decoder: Decoder): StringId<E> {
            return StringId(decoder.decodeString())
        }
    }
}

@Serializable(with = LongId.Serializer::class)
data class LongId<E : Any>(val value: Long) {

    @Suppress("UNUSED_PARAMETER")
    class Serializer<E : Any>(serializer: KSerializer<E>) : KSerializer<LongId<E>> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LongId", PrimitiveKind.LONG)

        override fun serialize(encoder: Encoder, value: LongId<E>) {
            encoder.encodeLong(value.value)
        }

        override fun deserialize(decoder: Decoder): LongId<E> {
            return LongId(decoder.decodeLong())
        }
    }
}
