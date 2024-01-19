package component.main.settings

import androidx.compose.material3.SnackbarHostState
import api.ApplicationApi
import api.model.UpdatePasswordRequest
import api.model.UpdateUsernameRequest
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.settings.implementation.*
import component.main.settings.util.SettingsOptions
import component.main.settings.warning.DefaultDeleteAccountComponent
import component.main.settings.warning.DeleteAccountComponent
import io.ktor.client.statement.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import settings.SettingsRepository
import util.Constants.NO_PASSWORD_PROVIDED
import util.Constants.PASSWORDS_NOT_MATCH
import util.Constants.PASSWORD_INCORRECT
import util.Constants.PASSWORD_MUST_BE_NEW
import util.Constants.USERNAME_EXISTS
import util.Status
import util.Status.Error
import util.Status.Success
import util.passwordToStatus
import util.usernameToStatus
import kotlin.coroutines.CoroutineContext

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val settings: SettingsRepository,
    override val onDismissed: () -> Unit,
    override val logout: () -> Unit
) : SettingsComponent, ComponentContext by componentContext {
    private val deleteAccountNavigation = SlotNavigation<DeleteAccountWarningConfig>()
    private val _deleteDialogSlot = childSlot(
        source = deleteAccountNavigation,
        serializer = DeleteAccountWarningConfig.serializer(),
        handleBackButton = true
    ) { _, childComponentContext ->
        DefaultDeleteAccountComponent(
            childComponentContext,
            deleteAccount = {
                deleteAccount(true, it)
                dismissDeleteAccountWarning()
            },
            dismiss = ::dismissDeleteAccountWarning
        )
    }
    override val deleteDialogSlot: Value<ChildSlot<*, DeleteAccountComponent>> = _deleteDialogSlot
    override fun showDeleteAccountWarning() = deleteAccountNavigation.activate(DeleteAccountWarningConfig)
    override fun dismissDeleteAccountWarning() = deleteAccountNavigation.dismiss()

    override val snackbarHostState = SnackbarHostState()

    private val _settingsOptions: MutableStateFlow<SettingsOptions?> = MutableStateFlow(null)
    override val settingsOptions: StateFlow<SettingsOptions?> = _settingsOptions

    override fun updateSettingsOptions(option: SettingsOptions?) = _settingsOptions.update { option }

    private val _statusUpdateStates = instanceKeeper.getOrCreate { StatusModelImpl(settings = settings, server = server) }
    override val statusLoading: Value<Boolean> = _statusUpdateStates.loadingState
    override val updateStatusStatus: Value<Status> = _statusUpdateStates.status
    private val _statusInput = MutableValue(settings.status.get())
    override val statusInput: Value<String> = _statusInput

    override suspend fun getStatus(context: CoroutineContext) = _statusUpdateStates.get(context)
    override suspend fun updateStatus(status: String, context: CoroutineContext) = withContext(context) {
        settings.status.set(status)
        _statusUpdateStates.update(status, context)
    }

    override fun updateStatusInput(with: String) = _statusInput.update { with }

    private val _cacheUpdateStates = instanceKeeper.getOrCreate { CacheModelImpl(settings, server) }
    override val cacheLoading: Value<Boolean> = _cacheUpdateStates.loadingState
    override val updateCacheStatus: Value<Status> = _cacheUpdateStates.status
    override suspend fun getCache(context: CoroutineContext) = _cacheUpdateStates.get(context)
    override suspend fun updateCache(cache: Boolean, context: CoroutineContext) {
        settings.cache.set(cache)
        _cacheUpdateStates.update(cache, context)
    }

    private val _updateUsernameStates = instanceKeeper.getOrCreate { UsernameModelImpl(server, settings) }
    override val usernameLoading: Value<Boolean> = _updateUsernameStates.loadingState
    override val usernameStatus: Value<Status> = _updateUsernameStates.status
    private val _usernameInput = MutableValue(settings.username.get())
    override val usernameInput: Value<String> = _usernameInput
    private val _usernameIsValid = MutableValue(true)
    override val usernameIsValid: Value<Boolean> = _usernameIsValid

    override fun updateUsernameStatus(status: Status) { _updateUsernameStates.updateStatus(status) }
    override fun getUsernameAsResponse(): String? = with(_updateUsernameStates.status.value) {
        takeIf { it is Error }?.let {
            when {
                (it as Error).body is HttpResponse ->
                    if ((it.body as HttpResponse).status == BadRequest) USERNAME_EXISTS else null
                it.body is String -> it.body
                else -> null
            }
        }
    }
    override suspend fun updateUsername(update: UpdateUsernameRequest, context: CoroutineContext) =
        _updateUsernameStates.apiCall(update, context)
    override fun updateUsernameInput(with: String) = _usernameInput.update { with }
    override fun validateUsernameInput(): Status =
        _usernameInput.value.usernameToStatus().also { status -> _usernameIsValid.update { status !is Error } }

    private val _updatePasswordStates = instanceKeeper.getOrCreate { PasswordModelImpl(server) }
    override val passwordLoading: Value<Boolean> = _updatePasswordStates.loadingState
    override val passwordStatus: Value<Status> = _updatePasswordStates.status
    override suspend fun updatePassword(update: UpdatePasswordRequest, context: CoroutineContext) =
        _updatePasswordStates.apiCall(update, context)

    private val _currentPassword = MutableValue("")
    override val currentPassword: Value<String> = _currentPassword
    private val _newPassword = MutableValue("")
    override val newPassword: Value<String> = _newPassword
    private val _confirmPassword = MutableValue("")
    override val confirmPassword: Value<String> = _confirmPassword
    private val _currentPasswordIsValid: MutableValue<Status> = MutableValue(Success)
    override val currentPasswordIsValid: Value<Status> = _currentPasswordIsValid
    private val _newPasswordIsValid: MutableValue<Status> = MutableValue(Success)
    override val newPasswordIsValid: Value<Status> = _newPasswordIsValid
    private val _confirmPasswordIsValid: MutableValue<Status> = MutableValue(Success)
    override val confirmPasswordIsValid: Value<Status> = _confirmPasswordIsValid

    override fun updateCurrentPassword(with: String) = _currentPassword.update { with }
    override fun updateNewPassword(with: String) = _newPassword.update { with }
    override fun updateConfirmPassword(with: String) = _confirmPassword.update { with }

    override suspend fun validateInputs(password: String, context: CoroutineContext): Unit = withContext(context) {
        val oldPasswordIsValid = async {
            _currentPasswordIsValid.update {
                if (_currentPassword.value == password) Success else Error(PASSWORD_INCORRECT)
            }
        }

        val newPasswordIsValid = async {
            _newPasswordIsValid.update {
                when {
                    _newPassword.value == password -> Error(PASSWORD_MUST_BE_NEW)
                    _newPassword.value.isBlank() -> Error(NO_PASSWORD_PROVIDED)
                    _newPassword.value != _confirmPassword.value -> Error(PASSWORDS_NOT_MATCH)
                    else -> _newPassword.value.passwordToStatus()
                }
            }
        }

        val confirmPasswordIsValid = async {
            _confirmPasswordIsValid.update {
                when {
                    _confirmPassword.value.isBlank() -> Error(NO_PASSWORD_PROVIDED)
                    _confirmPassword.value != _newPassword.value -> Error(PASSWORDS_NOT_MATCH)
                    else -> Success
                }
            }
        }

        awaitAll(oldPasswordIsValid, newPasswordIsValid, confirmPasswordIsValid)
    }

    override fun clearPasswords() {
        _currentPassword.update { "" }
        _newPassword.update { "" }
        _confirmPassword.update { "" }
    }

    private val _deleteAccountStates = instanceKeeper.getOrCreate { DeleteModelImpl(server) }
    override val deleteAccountLoading: Value<Boolean> = _deleteAccountStates.loadingState
    override val deleteAccountStatus: Value<Status> = _deleteAccountStates.status
    override suspend fun deleteAccount(delete: Boolean, context: CoroutineContext) = withContext(context) {
        async { _deleteAccountStates.apiCall(delete, context) }.await()
        if (_deleteAccountStates.status.value == Success) settings.clear()
    }

    @Serializable
    private data object DeleteAccountWarningConfig
}