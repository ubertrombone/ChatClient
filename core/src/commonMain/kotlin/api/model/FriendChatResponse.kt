package api.model

import kotlinx.serialization.Serializable

@Serializable
data class FriendChatResponse(
    val successful: Boolean,
    val message: FriendChatMessage
)
