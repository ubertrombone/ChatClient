package component.main.settings.implementation

import api.ApplicationApi
import api.callWrapper
import api.model.UpdateUsernameRequest
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.settings.interfaces.ApiModel
import kotlinx.coroutines.withContext
import util.Status
import util.Status.Error
import kotlin.coroutines.CoroutineContext

class UsernameModelImpl(
    initialLoading: Boolean,
    initialStatus: Status,
    private val server: ApplicationApi
) : ApiModel, InstanceKeeper.Instance {
    override val loadingState = MutableValue(initialLoading)
    override val status = MutableValue(initialStatus)

    override suspend fun <T> apiCall(value: T, context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.update(value as UpdateUsernameRequest) },
            onSuccess = { status.update { _ -> it } },
            onError = { status.update { _ -> Error(it) } }
        )
    }
}