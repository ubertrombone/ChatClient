package component.login

import api.ApplicationApi
import api.callWrapper
import api.model.AuthenticationRequest
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import settings.SettingsRepository
import util.MainPhases
import util.MainPhases.MAIN
import util.Status
import util.Status.*

class DefaultLoginComponent(
    componentContext: ComponentContext,
    override val settings: SettingsRepository,
    override val server: ApplicationApi,
    override val pushTo: (MainPhases) -> Unit
) : LoginComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main)

    override val title: String = "Login"

    private val _initStatus: MutableValue<Status> = MutableValue(Loading)
    override val initStatus: Value<Status> = _initStatus

    private val _loginStatus: MutableValue<Status> = MutableValue(Success)
    override val loginStatus: Value<Status> = _loginStatus

    private val _rememberMe: MutableValue<Boolean> = MutableValue(settings.rememberMe.get().toBooleanStrict())
    override val rememberMe: Value<Boolean> = _rememberMe

    private val _isInitLoading = MutableValue(true)
    override val isInitLoading: Value<Boolean> = _isInitLoading

    private val _isLoading = MutableValue(false)
    override val isLoading: Value<Boolean> = _isLoading

    override fun updateInit(status: Status) {
        scope.launch { _initStatus.update { status } }
    }
    override fun updateLogin(status: Status) {
        scope.launch { _loginStatus.update { status } }
    }
    override fun update(rememberMe: Boolean) {
        scope.launch {
            settings.rememberMe.set(rememberMe)
            _rememberMe.update { rememberMe }
        }
    }

    override fun initLogin() {
        scope.launch {
            callWrapper(
                isLoading = _isInitLoading,
                operation = server::login,
                onSuccess = {
                    if (it is Error) updateInit(status = Success)
                    if (it == Success) pushTo(MAIN)
                },
                onError = { updateInit(status = Error(it)) }
            )
        }
    }

    override fun login(credentials: AuthenticationRequest) {
        scope.launch {
            callWrapper(
                isLoading = _isLoading,
                operation = { server.authenticate(credentials) },
                onSuccess = {
                    if (it is Error) updateLogin(status = it)
                    if (it == Success) {
                        if (rememberMe.value) {
                            settings.username.set(credentials.username.name)
                            settings.password.set(credentials.password)
                        }
                        pushTo(MAIN)
                    }
                },
                onError = { updateLogin(status = Error(it)) }
            )
        }
    }
}