package api.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatEndPointResponse(
    val success: Boolean,
    val chatId: Int
)
