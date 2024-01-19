package api.model

import kotlinx.serialization.Serializable

@Serializable
data class GroupChatNameRequest(val name: String)