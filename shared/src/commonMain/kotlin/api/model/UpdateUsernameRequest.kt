package api.model

import kotlinx.serialization.Serializable
import util.Username

@Serializable
data class UpdateUsernameRequest(val newUsername: Username)