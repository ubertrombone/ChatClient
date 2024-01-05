package component.main.settings.implementation

import api.ApplicationApi
import api.callWrapper
import api.model.UpdatePasswordRequest
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.settings.interfaces.ApiModel
import kotlinx.coroutines.withContext
import util.Status
import kotlin.coroutines.CoroutineContext

class PasswordModelImpl(
    initialLoading: Boolean,
    initialStatus: Status,
    private val server: ApplicationApi
) : ApiModel, InstanceKeeper.Instance {
    override val loadingState = MutableValue(initialLoading)
    override val status = MutableValue(initialStatus)

    override suspend fun <T> apiCall(value: T, context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.update(value as UpdatePasswordRequest) },
            onSuccess = {  },
            onError = {  }
        )
    }
}