package api.model

import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class LoginRequest(
    val username: Username,
    val password: String
)