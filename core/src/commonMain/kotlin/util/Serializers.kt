@file:OptIn(ExperimentalSerializationApi::class)
@file:Suppress("EXTERNAL_SERIALIZER_USELESS")

package util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.SetSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.serialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

typealias SerializableImmutableSet<T> = @Serializable(ImmutableSetSerializer::class) ImmutableSet<T>
typealias SerializableImmutableList<T> = @Serializable(ImmutableListSerializer::class) ImmutableList<T>

@Serializer(forClass = ImmutableList::class)
class ImmutableListSerializer<T>(private val dataSerializer: KSerializer<T>) : KSerializer<ImmutableList<T>> {
    private class PersistentListDescriptor : SerialDescriptor by serialDescriptor<List<String>>() {
        override val serialName: String = "kotlinx.serialization.immutable.ImmutableList"
    }
    override val descriptor: SerialDescriptor = PersistentListDescriptor()

    override fun serialize(encoder: Encoder, value: ImmutableList<T>) =
        ListSerializer(dataSerializer).serialize(encoder, value.toList())

    override fun deserialize(decoder: Decoder): ImmutableList<T> =
        ListSerializer(dataSerializer).deserialize(decoder).toPersistentList()
}

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
