package util

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
sealed class Status {
    @Serializable data object Loading : Status()
    @Serializable data object Success : Status()
    @Serializable data class Error(val body: @Contextual Any) : Status()
}