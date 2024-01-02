package component.register

import api.ApplicationApi
import api.callWrapper
import api.model.AccountRequest
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.*
import settings.SettingsRepository
import util.*
import util.Constants.PASSWORDS_NOT_MATCH
import util.Constants.UNKNOWN_ERROR
import util.MainPhases.MAIN
import util.Status.Error
import util.Status.Success

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    override val settings: SettingsRepository,
    override val server: ApplicationApi,
    override val pushTo: (MainPhases) -> Unit
) : RegisterComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override val title: String = "Register"

    private val _isLoading = MutableValue(false)
    override val isLoading: Value<Boolean> = _isLoading

    private val _registrationStatus: MutableValue<Status> = MutableValue(Success)
    override val registrationStatus: Value<Status> = _registrationStatus

    private val _usernameStatus: MutableValue<Status> = MutableValue(Success)
    override val usernameStatus: Value<Status> = _usernameStatus

    private val _passwordStatus: MutableValue<Status> = MutableValue(Success)
    override val passwordStatus: Value<Status> = _passwordStatus

    private val _rememberMe = MutableValue(false)
    override val rememberMe: Value<Boolean> = _rememberMe

    override fun updateRegistration(status: Status) {
        scope.launch {
            _registrationStatus.update { status }
        }
    }

    override fun update(rememberMe: Boolean) {
        scope.launch {
            _rememberMe.update { rememberMe }
        }
    }

    private suspend fun validateUsername(username: String): Boolean = withContext(scope.coroutineContext) {
        username.usernameToStatus().also { _usernameStatus.update { _ -> it } } == Success
    }

    private suspend fun validatePassword(password: String, confirmation: String): Boolean = withContext(scope.coroutineContext) {
        if (password != confirmation) {
            _passwordStatus.update { Error(PASSWORDS_NOT_MATCH) }
            return@withContext false
        }

        password.toPasswordOrNull()?.let {
            _passwordStatus.update { Success }
            true
        } ?: run {
            _passwordStatus.update { Error(UNKNOWN_ERROR) }
            false
        }
    }

    override suspend fun register(account: AccountRequest) = withContext(scope.coroutineContext) {
        callWrapper(
            isLoading = _isLoading,
            operation = { server.register(account) },
            onSuccess = {
                if (it is Error) _registrationStatus.update { _ -> it }
                if (it == Success) pushTo(MAIN)
            },
            onError = { _registrationStatus.update { _ -> Error(it) } }
        )
    }

    override fun validateCredentials(username: String, password: String, confirmation: String) {
        scope.launch {
            val userValidator = async { validateUsername(username) }
            val passwordValidator = async { validatePassword(password, confirmation) }

            val awaitValidation = awaitAll(userValidator, passwordValidator)
            if (awaitValidation.all { it }) register(
                account = AccountRequest(
                    username = username.toUsername(),
                    password = password
                )
            )
        }
    }
}