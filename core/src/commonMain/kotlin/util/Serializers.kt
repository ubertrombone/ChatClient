@file:OptIn(ExperimentalSerializationApi::class)
@file:Suppress("EXTERNAL_SERIALIZER_USELESS")

package util

import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias SerializableImmutableSet<T> = @Serializable(ImmutableSetSerializer::class) ImmutableSet<T>

@Serializer(forClass = ImmutableSet::class)
class ImmutableSetSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<ImmutableSet<T>> {
    private class PersistentSetDescriptor : SerialDescriptor by serialDescriptor<Set<String>>() {
        override val serialName: String = "kotlinx.serialization.immutable.ImmutableSet"
    }
    override val descriptor: SerialDescriptor = PersistentSetDescriptor()

    override fun serialize(encoder: Encoder, value: ImmutableSet<T>) =
        SetSerializer(dataSerializer).serialize(encoder, value.toSet())

    override fun deserialize(decoder: Decoder): ImmutableSet<T> =
        SetSerializer(dataSerializer).deserialize(decoder).toPersistentSet()
}
