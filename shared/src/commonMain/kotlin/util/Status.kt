package util

sealed class Status {
    data object Loading : Status()
    data object Success : Status()
    data class Error(val message: String) : Status()
}