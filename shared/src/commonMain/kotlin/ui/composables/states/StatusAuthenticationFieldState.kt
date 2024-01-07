package ui.composables.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import util.Constants.MAX_STATUS_LENGTH
import util.Constants.STATUS_TOO_LONG
import util.Status
import util.Status.Error
import util.Status.Success

class StatusAuthenticationFieldState(input: String = "", isValid: Boolean = true) : AuthenticationFieldState {
    private val _input = MutableValue(input)
    override val input: Value<String> = _input

    private val _isValid = MutableValue(isValid)
    override val isValid: Value<Boolean> = _isValid

    fun validateInput(): Status = with(_input.value.length > MAX_STATUS_LENGTH) {
        _isValid.update { this }
        if (this) Error(STATUS_TOO_LONG) else Success
    }

    override fun updateInput(with: String) = _input.update { with }

    companion object {
        val saver = Saver<StatusAuthenticationFieldState, List<Any>>(
            save = { listOf(it._input.value, it._isValid.value) },
            restore = {
                StatusAuthenticationFieldState(
                    input = it[0] as String,
                    isValid = it[1] as Boolean
                )
            }
        )
    }
}

@Composable
fun rememberStatusAuthenticationFieldState(input: String): StatusAuthenticationFieldState = rememberSaveable(
    saver = StatusAuthenticationFieldState.saver
) { StatusAuthenticationFieldState(input = input) }