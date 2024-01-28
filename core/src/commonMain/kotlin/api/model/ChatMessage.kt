package api.model

import kotlinx.serialization.Serializable
import util.Functions
import util.Functions.INDIVIDUAL
import util.Username

@Serializable
data class ChatMessage(
    val function: Functions = INDIVIDUAL,
    val sender: Username,
    val recipientOrGroup: String,
    val message: String,
    val error: String? = null
)
