package api.model

import kotlinx.serialization.Serializable

@Serializable
data class OpenChatRequest(
    val sender: Int,
    val recipient: Int
)
