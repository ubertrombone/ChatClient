package component.main.settings.status

import api.ApplicationApi
import api.callWrapper
import api.model.StatusRequest
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
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
) : StatusModel, InstanceKeeper.Instance {
    override val scope = CoroutineScope(Dispatchers.Main)
    override val loadingState = MutableValue(initialLoading)
    override val statusStatus = MutableValue(initialStatus)

    override suspend fun getStatus(context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.getStatus() },
            onSuccess = { status ->
                with(settings.status.get()) {
                    if ((status ?: "") != this) scope.launch { updateStatus(this@with, this.coroutineContext) }
                }
            },
            onError = { statusStatus.update { _ -> Error(it) } }
        )
    }

    override suspend fun updateStatus(status: String, context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.update(StatusRequest(status)) },
            onSuccess = {
                if (it == Status.Success) statusStatus.update { Status.Success }
                if (it is Error) statusStatus.update { _ -> it }
            },
            onError = { statusStatus.update { _ -> Error(it) } }
        )
    }

    override fun onDestroy() = scope.cancel()
}