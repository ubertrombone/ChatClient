package api.model

import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class FriendRequest(
    val id: Int,
    val requesterUsername: Username,
    val toUsername: Username
)