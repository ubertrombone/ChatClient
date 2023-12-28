package api.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class FriendInfo(
    val username: Username,
    val isOnline: Boolean,
    val status: String?,
    val lastOnline: Instant?
)