package component.main.add.model

import api.model.FriendRequest
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable
import util.SerializableImmutableSet

@Serializable
data class Requests(val reqs: SerializableImmutableSet<FriendRequest> = persistentSetOf())
