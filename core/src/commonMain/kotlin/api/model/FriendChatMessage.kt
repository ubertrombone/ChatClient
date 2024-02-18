package api.model

import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class FriendChatMessage(
    val sender: Username,
    val recipient: Username,
    val message: String,
    val error: String? = null
)
