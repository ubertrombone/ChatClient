package component.main.add.requests.sent

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import util.Status
import util.Status.Error
import util.Username

class CancelModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    private val server: ApplicationApi,
    private val authCallback: () -> Unit
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)

    fun cancelRequest(username: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.cancelFriendRequest(username) },
                onSuccess = { status.update { _ -> it } },
                onError = {
                    status.update { _ -> Error(it) }
                    if (it == Unauthorized.description) authCallback()
                }
            )
        }
    }

    override fun onDestroy() = scope.cancel()
}