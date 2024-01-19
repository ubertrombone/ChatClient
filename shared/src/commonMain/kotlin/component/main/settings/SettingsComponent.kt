package component.main.settings

import androidx.compose.material3.SnackbarHostState
import api.ApplicationApi
import api.model.UpdatePasswordRequest
import api.model.UpdateUsernameRequest
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import component.main.settings.warning.DeleteAccountComponent
import kotlinx.coroutines.flow.StateFlow
import settings.SettingsRepository
import component.main.settings.util.SettingsOptions
import util.Status
import kotlin.coroutines.CoroutineContext

interface SettingsComponent {
    val settings: SettingsRepository
    val server: ApplicationApi
    val onDismissed: () -> Unit
    val logout: () -> Unit

    val deleteDialogSlot: Value<ChildSlot<*, DeleteAccountComponent>>

    fun showDeleteAccountWarning()
    fun dismissDeleteAccountWarning()

    val snackbarHostState: SnackbarHostState

    val settingsOptions: StateFlow<SettingsOptions?>

    fun updateSettingsOptions(option: SettingsOptions?)

    val statusLoading: Value<Boolean>
    val updateStatusStatus: Value<Status>
    val statusInput: Value<String>
    suspend fun getStatus(context: CoroutineContext)
    suspend fun updateStatus(status: String, context: CoroutineContext)
    fun updateStatusInput(with: String)

    val cacheLoading: Value<Boolean>
    val updateCacheStatus: Value<Status>
    suspend fun getCache(context: CoroutineContext)
    suspend fun updateCache(cache: Boolean, context: CoroutineContext)

    val usernameLoading: Value<Boolean>
    val usernameStatus: Value<Status>
    val usernameInput: Value<String>
    val usernameIsValid: Value<Boolean>
    fun updateUsernameStatus(status: Status)
    fun getUsernameAsResponse(): String?
    suspend fun updateUsername(update: UpdateUsernameRequest, context: CoroutineContext)
    fun updateUsernameInput(with: String)
    fun validateUsernameInput(): Status

    val passwordLoading: Value<Boolean>
    val passwordStatus: Value<Status>
    suspend fun updatePassword(update: UpdatePasswordRequest, context: CoroutineContext)

    val currentPassword: Value<String>
    val newPassword: Value<String>
    val confirmPassword: Value<String>
    val currentPasswordIsValid: Value<Status>
    val newPasswordIsValid: Value<Status>
    val confirmPasswordIsValid: Value<Status>

    fun updateCurrentPassword(with: String)
    fun updateNewPassword(with: String)
    fun updateConfirmPassword(with: String)
    suspend fun validateInputs(password: String, context: CoroutineContext)
    fun clearPasswords()

    val deleteAccountLoading: Value<Boolean>
    val deleteAccountStatus: Value<Status>
    suspend fun deleteAccount(delete: Boolean, context: CoroutineContext)
}