package component.main.add.requests.received

import api.ApplicationApi
import api.callWrapper
import api.model.FriendRequest
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
import util.Status.Success

class AcceptModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    private val server: ApplicationApi,
    private val authCallback: () -> Unit
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)

    fun addFriend(request: FriendRequest) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.add(request.requesterUsername) },
                onSuccess = { result ->
                    if (result == Success) scope.launch { closeRequest(request) }
                    else status.update { result }
                },
                onError = {
                    status.update { _ -> Error(it) }
                    if (it == Unauthorized.description) authCallback()
                }
            )
        }
    }

    fun closeRequest(request: FriendRequest) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.removeFriendRequest(request) },
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