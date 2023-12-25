package ui.composables.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update

class RegisterAuthenticationFieldState(input: String = "", isValid: Boolean = true) : AuthenticationFieldState {

    private val _input = MutableValue(input)
    override val input: Value<String> = _input

    private val _isValid = MutableValue(isValid)
    override val isValid: Value<Boolean> = _isValid

    override fun updateInput(with: String) { _input.update { with } }
    fun updateIsValid(with: Boolean) { _isValid.update { with } }

    companion object {
        val saver = Saver<RegisterAuthenticationFieldState, List<Any>>(
            save = { listOf(it._input.value, it._isValid.value) },
            restore = {
                RegisterAuthenticationFieldState(
                    input = it[0] as String,
                    isValid = it[1] as Boolean
                )
            }
        )
    }
}

@Composable
fun rememberRegisterAuthenticationFieldState(input: String): RegisterAuthenticationFieldState = rememberSaveable(
    saver = RegisterAuthenticationFieldState.saver
) {
    RegisterAuthenticationFieldState(input = input)
}