package component.main.add

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.ktor.client.statement.*
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import util.Constants.REQUEST_ALREADY_RECEIVED
import util.Status
import util.Status.Error
import util.Username

class ActionModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    private val server: ApplicationApi,
    private val authCallback: () -> Unit
): InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)

    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)

    fun sendFriendRequest(to: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.sendFriendRequest(to) },
                onSuccess = {
                    if (it is Error) {
                        scope.launch {
                            if ((it.body as HttpResponse).bodyAsText() == REQUEST_ALREADY_RECEIVED)
                                addFriend(to)
                        }
                    } else status.update { _ -> it }
                },
                onError = {
                    status.update { _ -> Error(it) }
                    if (it == Unauthorized.description) authCallback()
                }
            )
        }
    }

    fun addFriend(username: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.add(username) },
                onSuccess = { status.update { it } },
                onError = {
                    status.update { _ -> Error(it) }
                    if (it == Unauthorized.description) authCallback()
                }
            )
        }
    }

    fun removeFriend(username: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.remove(username) },
                onSuccess = { status.update { it } },
                onError = {
                    status.update { _ -> Error(it) }
                    if (it == Unauthorized.description) authCallback()
                }
            )
        }
    }

    fun blockFriend(username: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.block(username) },
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