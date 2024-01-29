package api.model

import kotlinx.serialization.Serializable

@Serializable
data class SendChatResponse(
    val successful: Boolean,
    val message: ChatMessage
)
