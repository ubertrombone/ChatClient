package component.login

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.serializer
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

    private val _status: MutableValue<Status> = MutableValue(Loading)
    override val status: Value<Status> = _status

    private val _rememberMe: MutableValue<Boolean> = MutableValue(settings.rememberMe.get().toBooleanStrict())
    override val rememberMe: Value<Boolean> = _rememberMe

    private val _username = instanceKeeper.getOrCreate(USERNAME_INSTANCE) {
        UsernameModelImpl(
            stateKeeper.consume(key = USERNAME_STATE, strategy = String.serializer())
                ?: if (_rememberMe.value) settings.username.get() else ""
        )
    }
    override val username: Value<String> = _username.username

    override fun update(status: Status) {
        scope.launch { _status.update { status } }
    }

    override fun update(username: String) { _username::update }
    override fun update(rememberMe: Boolean) {
        scope.launch {
            settings.rememberMe.set(rememberMe)
            _rememberMe.update { rememberMe }
        }
    }

    private val _isInitLoading = MutableValue(true)
    override val isInitLoading: Value<Boolean> = _isInitLoading

    private val _isLoading = MutableValue(false)
    override val isLoading: Value<Boolean> = _isLoading

    override fun initLogin() {
        scope.launch {
            callWrapper(
                isLoading = _isInitLoading,
                operation = server::login,
                onSuccess = {
                    if (it is Error) update(status = Success)
                    if (it == Success) pushTo(MAIN)
                },
                onError = { update(status = Error(it)) }
            )
        }
    }

    override fun login() {
        scope.launch {
            callWrapper(
                isLoading = _isLoading, // TODO: Implement _status here somehow
                operation = server::login,
                onSuccess = {
                    when (it) {
                        is Error -> TODO("Show red, somehow integrate reason")
                        Loading -> TODO("Won't be Loading")
                        Success -> pushTo(MAIN)
                    }
                },
                onError = null // TODO
            )
        }
    }

    init {
        stateKeeper.register(
            key = USERNAME_STATE,
            strategy = String.serializer()
        ) { _username.username.value }
    }

    private companion object {
        private const val USERNAME_INSTANCE = "USERNAME_INSTANCE"
        private const val USERNAME_STATE = "USERNAME_STATE"
    }
}