package api.model

import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class AccountRequest(
    val username: Username,
    val password: String
)