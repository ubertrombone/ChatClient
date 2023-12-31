package component.main.friends.model

import api.model.FriendInfo
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.serialization.Serializable
import util.SerializableImmutableSet

@Serializable
data class FriendsSet(val friends: SerializableImmutableSet<FriendInfo> = persistentSetOf())
