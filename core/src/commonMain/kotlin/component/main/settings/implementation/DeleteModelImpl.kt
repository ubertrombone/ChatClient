package component.main.settings.implementation

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.settings.interfaces.ApiModel
import kotlinx.coroutines.withContext
import util.Status
import util.Status.Error
import util.Status.Loading
import kotlin.coroutines.CoroutineContext

class DeleteModelImpl(private val server: ApplicationApi) : ApiModel, InstanceKeeper.Instance {
    override val loadingState = MutableValue(false)
    override val status: MutableValue<Status> = MutableValue(Loading)

    override fun updateStatus(value: Status) = status.update { value }

    override suspend fun <T> apiCall(value: T, context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.deleteAccount(value as Boolean) },
            onSuccess = { status.update { _ -> it } },
            onError = { status.update { _ -> Error(it) } }
        )
    }
}