package ui.composables.states

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import util.Constants.NO_PASSWORD_PROVIDED
import util.Constants.PASSWORDS_NOT_MATCH
import util.Constants.PASSWORD_INCORRECT
import util.Constants.PASSWORD_MUST_BE_NEW
import util.Status
import util.Status.Error
import util.Status.Success
import util.passwordToStatus
import kotlin.coroutines.CoroutineContext

class PasswordAuthenticationFieldState(
    oldPassword: String = "",
    newPassword: String = "",
    confirmPassword: String = ""
) {
    private val _oldPasswordInput = MutableValue(oldPassword)
    val oldPasswordInput: Value<String> = _oldPasswordInput

    private val _newPasswordInput = MutableValue(newPassword)
    val newPasswordInput: Value<String> = _newPasswordInput

    private val _confirmPasswordInput = MutableValue(confirmPassword)
    val confirmPasswordInput: Value<String> = _confirmPasswordInput

    private val _oldPasswordIsValid: MutableValue<Status> = MutableValue(Success)
    val oldPasswordIsValid: Value<Status> = _oldPasswordIsValid

    private val _newPasswordIsValid: MutableValue<Status> = MutableValue(Success)
    val newPasswordIsValid: Value<Status> = _newPasswordIsValid

    private val _confirmPasswordIsValid: MutableValue<Status> = MutableValue(Success)
    val confirmPasswordIsValid: Value<Status> = _confirmPasswordIsValid

    fun updateOldInput(with: String) = _oldPasswordInput.update { with }
    fun updateNewInput(with: String) = _newPasswordInput.update { with }
    fun updateConfirmInput(with: String) = _confirmPasswordInput.update { with }

    suspend fun validateInputs(password: String, context: CoroutineContext) = withContext(context) {
        val oldPasswordIsValid = async {
            _oldPasswordIsValid.update {
                if (_oldPasswordInput.value == password) Success else Error(PASSWORD_INCORRECT)
            }
        }

        val newPasswordIsValid = async {
            _newPasswordIsValid.update {
                when {
                    _newPasswordInput.value == password -> Error(PASSWORD_MUST_BE_NEW)
                    _newPasswordInput.value.isBlank() -> Error(NO_PASSWORD_PROVIDED)
                    _newPasswordInput.value != _confirmPasswordInput.value -> Error(PASSWORDS_NOT_MATCH)
                    else -> _newPasswordInput.value.passwordToStatus()
                }
            }
        }

        val confirmPasswordIsValid = async {
            _confirmPasswordIsValid.update {
                when {
                    _confirmPasswordInput.value.isBlank() -> Error(NO_PASSWORD_PROVIDED)
                    _confirmPasswordInput.value != _newPasswordInput.value -> Error(PASSWORDS_NOT_MATCH)
                    else -> Success
                }
            }
        }

        awaitAll(oldPasswordIsValid, newPasswordIsValid, confirmPasswordIsValid)
    }

    companion object {
        val saver = Saver<PasswordAuthenticationFieldState, List<Any>>(
            save = { listOf(it._oldPasswordInput.value, it._newPasswordInput.value, it._confirmPasswordInput.value) },
            restore = {
                PasswordAuthenticationFieldState(
                    oldPassword = it[0] as String,
                    newPassword = it[1] as String,
                    confirmPassword = it[2] as String
                )
            }
        )
    }
}

@Composable
fun rememberPasswordAuthenticationFieldState(): PasswordAuthenticationFieldState = rememberSaveable(
    saver = PasswordAuthenticationFieldState.saver
) { PasswordAuthenticationFieldState() }