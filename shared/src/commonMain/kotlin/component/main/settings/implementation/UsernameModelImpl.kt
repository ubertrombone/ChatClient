package component.main.settings.implementation

import api.ApplicationApi
import api.callWrapper
import api.model.AuthenticationRequest
import api.model.UpdateUsernameRequest
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.settings.interfaces.ApiModel
import kotlinx.coroutines.*
import settings.SettingsRepository
import util.Status
import util.Status.*
import kotlin.coroutines.CoroutineContext

class UsernameModelImpl(
    private val server: ApplicationApi,
    private val settings: SettingsRepository
) : ApiModel, InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.IO)
    override val loadingState = MutableValue(false)
    override val status: MutableValue<Status> = MutableValue(Loading)

    override fun updateStatus(value: Status) { status.update { value } }

    override suspend fun <T> apiCall(value: T, context: CoroutineContext) = withContext(context) {
        with (value as UpdateUsernameRequest) {
            callWrapper(
                isLoading = loadingState,
                operation = { server.update(this) },
                onSuccess = {
                    status.update { _ -> it }
                    if (it == Success) scope.launch {
                        server.authenticate(
                            AuthenticationRequest(
                                username = newUsername,
                                password = settings.password.get()
                            )
                        )
                            .also { auth -> if (auth == Success) settings.username.set(newUsername.name) }
                    }
                },
                onError = { status.update { _ -> Error(it) } }
            )
        }
    }
}