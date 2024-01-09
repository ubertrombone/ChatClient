package component.main.add

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import util.Status
import util.Status.Error
import util.Username

class ActionModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    private val server: ApplicationApi
): InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)

    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)

    fun sendFriendRequest(to: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.sendFriendRequest(to) },
                onSuccess = { status.update { it } },
                onError = { status.update { _ -> Error(it) } }
            )
        }
    }

    fun addFriend(username: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.add(username) },
                onSuccess = { status.update { it } },
                onError = { status.update { _ -> Error(it) } }
            )
        }
    }

    fun removeFriend(username: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.remove(username) },
                onSuccess = { status.update { it } },
                onError = { status.update { _ -> Error(it) } }
            )
        }
    }

    override fun onDestroy() = scope.cancel()
}