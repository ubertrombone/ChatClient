package ui.composables.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import util.Status
import util.usernameToStatus

class UsernameAuthenticationFieldState(input: String = "", isValid: Boolean = true) : AuthenticationFieldState {
    private val _input = MutableValue(input)
    override val input: Value<String> = _input

    private val _isValid = MutableValue(isValid)
    override val isValid: Value<Boolean> = _isValid

    fun validateInput(): Status =
        _input.value.usernameToStatus().also { status -> _isValid.update { status !is Status.Error } }

    override fun updateInput(with: String) = _input.update { with }

    companion object {
        val saver = Saver<UsernameAuthenticationFieldState, List<Any>>(
            save = { listOf(it._input.value, it._isValid.value) },
            restore = {
                UsernameAuthenticationFieldState(
                    input = it[0] as String,
                    isValid = it[1] as Boolean
                )
            }
        )
    }
}

@Composable
fun rememberUsernameAuthenticationFieldState(input: String): UsernameAuthenticationFieldState = rememberSaveable(
    saver = UsernameAuthenticationFieldState.saver
) {
    UsernameAuthenticationFieldState(input = input)
}