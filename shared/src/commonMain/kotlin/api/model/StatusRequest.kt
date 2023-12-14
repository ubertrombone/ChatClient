package api.model

import kotlinx.serialization.Serializable

@Serializable
data class StatusRequest(val status: String?)