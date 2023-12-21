package component.login

import androidx.compose.runtime.mutableStateOf
import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
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

    private val _username = instanceKeeper.getOrCreate(USERNAME_INSTANCE) {
        UsernameModelImpl(stateKeeper.consume(key = USERNAME_STATE, strategy = String.serializer()) ?: "")
    }
    override val username: Value<String> = _username.username

    private val _rememberMe: MutableValue<Boolean> = MutableValue(settings.rememberMe.get().toBooleanStrict())
    override val rememberMe: Value<Boolean> = _rememberMe

    override fun update(status: Status) {
        scope.launch { _status.update { status } }
    }

    override fun update(username: String) { _username::update }
    override fun update(rememberMe: Boolean) {
        scope.launch { _rememberMe.update { rememberMe } }
    }

    // TODO: Add state for remember me and populate textfields if true
    override fun login() {
        scope.launch {
            callWrapper(
                isLoading = mutableStateOf(true), // TODO: Implement _status here somehow
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