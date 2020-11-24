package com.eighthours.sample.domain.common.serialization

import com.eighthours.sample.domain.common.LongId
import com.eighthours.sample.domain.common.StringId
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Suppress("UNUSED_PARAMETER")
class StringIdSerializer<E : Any>(serializer: KSerializer<E>) : KSerializer<StringId<E>> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("StringId", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: StringId<E>) {
        encoder.encodeString(value.value)
    }

    override fun deserialize(decoder: Decoder): StringId<E> {
        return StringId(decoder.decodeString())
    }
}

@Suppress("UNUSED_PARAMETER")
class LongIdSerializer<E : Any>(serializer: KSerializer<E>) : KSerializer<LongId<E>> {

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LongId", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: LongId<E>) {
        encoder.encodeLong(value.value)
    }

    override fun deserialize(decoder: Decoder): LongId<E> {
        return LongId(decoder.decodeLong())
    }
}
