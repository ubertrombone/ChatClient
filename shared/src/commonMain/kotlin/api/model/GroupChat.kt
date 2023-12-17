package api.model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class GroupChat(
    val name: String,
    val creator: Username,
    val createdDate: Instant,
    val members: Set<Username>
)