package component.login

import api.ApplicationApi
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
import util.MainPhases
import util.Status
import util.Status.Loading

class DefaultLoginComponent(
    componentContext: ComponentContext,
    override val token: String,
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

    override fun update(status: Status) {
        scope.launch { _status.update { status } }
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