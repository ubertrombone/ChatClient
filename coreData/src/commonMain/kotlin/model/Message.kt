package model

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val message: String,
    val sender: Int,
    val timestamp: Instant,
    val primaryUserRef: Int,
    val chat: Int
)
