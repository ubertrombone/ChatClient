package component.main.friends

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.friends.model.FriendsSet
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.*
import util.Constants.UNKNOWN_ERROR
import util.Status
import util.Status.Error
import util.Status.Success

class FriendsModelImpl(
    initialState: FriendsSet,
    initialLoadingState: Boolean,
    initialStatus: Status,
    private val server: ApplicationApi,
    private val authCallback: () -> Unit
) : FriendsModel, InstanceKeeper.Instance {
    override val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    override val friendsListState = MutableValue(initialState)
    override val friendsListLoading = MutableValue(initialLoadingState)
    override val friendsListStatus = MutableValue(initialStatus)

    init {
        scope.launch {
            delay(500)
            friendLooper()
        }
    }

    override suspend fun friendLooper() = withContext(scope.coroutineContext) {
        while (scope.isActive) {
            getFriends()
            delay(30_000)
        }
    }

    override suspend fun getFriends() = withContext(scope.coroutineContext) {
        callWrapper(
            isLoading = friendsListLoading,
            operation = { server.getFriends() },
            onSuccess = { friends ->
                friends?.let { f ->
                    friendsListState.update { FriendsSet(friends = f.toImmutableSet()) }
                    friendsListStatus.update { Success }
                } ?: friendsListStatus.update { Error(UNKNOWN_ERROR) }
            },
            onError = {
                friendsListStatus.update { _ -> Error(it) }
                if (it == Unauthorized.description) authCallback()
            }
        )
    }

    override fun onDestroy() = scope.cancel()
}