package api.model

import kotlinx.serialization.Serializable
import util.SerializableImmutableList

@Serializable
data class ChatCache(val cache: SerializableImmutableList<FriendChatMessage>)
