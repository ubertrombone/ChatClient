package model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int? = null,
    val message: String,
    val sender: String,
    val timestamp: Instant,
    val primaryUserRef: String,
    val chat: Int
)
