package ui.composables.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import ui.composables.states.Types.PASSWORD
import ui.composables.states.Types.USERNAME
import util.Constants.INVALID_USERNAME
import util.Constants.NO_PASSWORD_PROVIDED
import util.Status
import util.Status.Error
import util.Status.Success

class LoginAuthenticationFieldState(input: String = "", isValid: Boolean = true) : AuthenticationFieldState {
    private val _input = MutableValue(input)
    override val input: Value<String> = _input
    
    private val _isValid = MutableValue(isValid)
    override val isValid: Value<Boolean> = _isValid

    fun validateInput(response: Status, type: Types) {
        if (response is Error) {
            _isValid.update {
                when {
                    type == USERNAME && response.message == NO_PASSWORD_PROVIDED -> true
                    type == PASSWORD && response.message == INVALID_USERNAME -> true
                    else -> false
                }
            }
        }
        if (response == Success) _isValid.update { true }
    }
    override fun updateInput(with: String) { _input.update { with } }
    
    companion object {
        val saver = Saver<LoginAuthenticationFieldState, List<Any>>(
            save = { listOf(it._input.value, it._isValid.value) },
            restore = {
                LoginAuthenticationFieldState(
                    input = it[0] as String,
                    isValid = it[1] as Boolean
                )
            }
        )
    }
}

enum class Types {
    USERNAME,
    PASSWORD
}

@Composable
fun rememberLoginAuthenticationFieldState(initialInput: String): LoginAuthenticationFieldState = rememberSaveable(
    saver = LoginAuthenticationFieldState.saver
) {
    LoginAuthenticationFieldState(input = initialInput)
}