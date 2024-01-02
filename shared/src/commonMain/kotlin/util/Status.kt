package util

sealed class Status {
    data object Loading : Status()
    data object Success : Status()
    data class Error<T>(val body: T) : Status()
}