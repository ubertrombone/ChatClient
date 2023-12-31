package api

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import io.github.aakira.napier.Napier

suspend fun <T> callWrapper(
    isLoading: MutableValue<Boolean>,
    operation: suspend () -> T,
    onSuccess: (T) -> Unit,
    onError: ((String) -> Unit)?
) {
    isLoading.update { true }
    try {
        val result = operation()
        onSuccess(result)
    } catch (e: Exception) {
        val error = e.message ?: "An unknown error has occurred."
        Napier.e(message = error)
        onError?.invoke(error)
    } finally {
        isLoading.update { false }
    }
}