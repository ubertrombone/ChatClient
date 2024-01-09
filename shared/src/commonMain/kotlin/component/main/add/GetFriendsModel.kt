package component.main.add

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.add.model.Friends
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import util.Constants
import util.Status

class GetFriendsModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    initialState: Friends,
    private val server: ApplicationApi
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)

    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)
    val result = MutableValue(initialState)

    fun apiCall() {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.getFriends() },
                onSuccess = { friends ->
                    friends?.let {
                        result.update { Friends(friends = friends.map { it.username }.toImmutableSet()) }
                        status.update { Status.Success }
                    } ?: status.update { Status.Error(Constants.UNKNOWN_ERROR) }
                },
                onError = { status.update { _ -> Status.Error(it) } }
            )
        }
    }

    override fun onDestroy() = scope.cancel()
}