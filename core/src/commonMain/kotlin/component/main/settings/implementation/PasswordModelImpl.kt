package component.main.settings.implementation

import api.ApplicationApi
import api.callWrapper
import api.model.UpdatePasswordRequest
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.settings.interfaces.ApiModel
import kotlinx.coroutines.withContext
import util.Status
import util.Status.Error
import util.Status.Loading
import kotlin.coroutines.CoroutineContext

class PasswordModelImpl(private val server: ApplicationApi) : ApiModel, InstanceKeeper.Instance {
    override val loadingState = MutableValue(false)
    override val status: MutableValue<Status> = MutableValue(Loading)

    override fun updateStatus(value: Status) = status.update { value }

    override suspend fun <T> apiCall(value: T, context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.update(value as UpdatePasswordRequest) },
            onSuccess = { status.update { _ -> it } },
            onError = { status.update { _ -> Error(it) } }
        )
    }
}