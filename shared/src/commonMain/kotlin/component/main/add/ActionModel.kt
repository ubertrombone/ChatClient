package component.main.add

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import util.Status
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
                onSuccess = {  },
                onError = {  }
            )
        }
    }

    fun addFriend(username: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.add(username) },
                onSuccess = {  },
                onError = {  }
            )
        }
    }

    fun removeFriend(username: Username) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.remove(username) },
                onSuccess = {  },
                onError = {  }
            )
        }
    }

    override fun onDestroy() = scope.cancel()
}