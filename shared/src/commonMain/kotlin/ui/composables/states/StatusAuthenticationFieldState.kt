package ui.composables.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class StatusAuthenticationFieldState(input: String = "") {
    private val _input = MutableStateFlow(input)
    val input: StateFlow<String> = _input

    fun updateInput(with: String) = _input.update { with }

    companion object {
        val saver = Saver<StatusAuthenticationFieldState, List<Any>>(
            save = { listOf(it._input.value) },
            restore = { StatusAuthenticationFieldState(input = it[0] as String) }
        )
    }
}

@Composable
fun rememberStatusAuthenticationFieldState(input: String): StatusAuthenticationFieldState = rememberSaveable(
    saver = StatusAuthenticationFieldState.saver
) { StatusAuthenticationFieldState(input = input) }