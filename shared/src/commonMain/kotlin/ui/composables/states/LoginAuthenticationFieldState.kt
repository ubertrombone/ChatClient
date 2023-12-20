package ui.composables.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update

class LoginAuthenticationFieldState(input: String = "", isValid: Boolean = true) : AuthenticationFieldState {
    private var _input = MutableValue(input)
    override var input = _input
    
    private var _isValid = MutableValue(isValid)
    override val isValid = _isValid
    
    fun validateInput(response: Boolean) { _isValid.update { response } }

    override fun updateInput(with: String) { _input.update { with } }
    
    companion object {
        val saver = Saver<LoginAuthenticationFieldState, List<Any>>(
            save = { listOf(it._input, it._isValid) },
            restore = {
                LoginAuthenticationFieldState(
                    input = it[0] as String,
                    isValid = it[1] as Boolean
                )
            }
        )
    }
}

@Composable
fun rememberLoginAuthenticationFieldState(initialInput: String): AuthenticationFieldState = rememberSaveable(
    saver = LoginAuthenticationFieldState.saver
) {
    LoginAuthenticationFieldState(input = initialInput)
}