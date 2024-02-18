package api.model

import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class OpenChatRequest(
    val sender: Username,
    val recipient: Username
)
