package api

import androidx.compose.runtime.MutableState
import io.github.aakira.napier.Napier

suspend fun <T> callWrapper(
    isLoading: MutableState<Boolean>,
    operation: suspend () -> T,
    onSuccess: (T) -> Unit,
    onError: (() -> Unit)?
) {
    isLoading.value = true
    try {
        val result = operation()
        onSuccess(result)
    } catch (e: Exception) {
        val error = e.message ?: "An unknown error has occurred."
        Napier.e(message = error, throwable = e)
        onError?.invoke()
    } finally {
        isLoading.value = false
    }
}