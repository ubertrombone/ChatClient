package api.model

import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class AuthenticationRequest(
    val username: Username,
    val password: String
)