package component.main.add.model

import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable
import util.SerializableImmutableSet
import util.Username

@Serializable
data class Friends(val friends: SerializableImmutableSet<Username> = persistentSetOf())
