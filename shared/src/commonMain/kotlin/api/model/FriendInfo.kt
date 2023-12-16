package api.model

import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class FriendInfo(
    val username: Username,
    val isOnline: Boolean,
    val lastOnline: String?
)