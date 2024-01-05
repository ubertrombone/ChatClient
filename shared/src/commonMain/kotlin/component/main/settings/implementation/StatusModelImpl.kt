package component.main.settings.implementation

import api.ApplicationApi
import api.callWrapper
import api.model.StatusRequest
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.settings.`interface`.LocalModel
import kotlinx.coroutines.*
import settings.SettingsRepository
import util.Status
import util.Status.Error
import kotlin.coroutines.CoroutineContext

class StatusModelImpl(
    initialLoading: Boolean,
    initialStatus: Status,
    private val settings: SettingsRepository,
    private val server: ApplicationApi
) : LocalModel, InstanceKeeper.Instance {
    override val scope = CoroutineScope(Dispatchers.Main)
    override val loadingState = MutableValue(initialLoading)
    override val status = MutableValue(initialStatus)

    override suspend fun get(context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.getStatus() },
            onSuccess = { status ->
                with(settings.status.get()) {
                    if ((status ?: "") != this) scope.launch { update(this@with, this.coroutineContext) }
                }
            },
            onError = { status.update { _ -> Error(it) } }
        )
    }

    override suspend fun <T> update(value: T, context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.update(StatusRequest(value as String)) },
            onSuccess = {
                if (it == Status.Success) status.update { Status.Success }
                if (it is Error) status.update { _ -> it }
            },
            onError = { status.update { _ -> Error(it) } }
        )
    }

    override fun onDestroy() = scope.cancel()
}