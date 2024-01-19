package component.main.add.requests.received

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.add.model.Requests
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.*
import util.Constants.UNKNOWN_ERROR
import util.Status
import util.Status.Error
import util.Status.Success

class ReceiveListModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    initialState: Requests,
    private val server: ApplicationApi,
    private val authCallback: () -> Unit
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)
    val result = MutableValue(initialState)

    init {
        scope.launch {
            delay(500)
            looper()
        }
    }

    private suspend fun looper() = withContext(scope.coroutineContext) {
        while (scope.isActive) {
            getRequests()
            delay(10_000)
        }
    }

    fun getRequests() {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.getReceivedFriendRequests() },
                onSuccess = { results ->
                    results?.let { r ->
                        result.update { Requests(r.toImmutableSet()) }
                        status.update { Success }
                    } ?: status.update { Error(UNKNOWN_ERROR) }
                },
                onError = {
                    status.update { _ -> Error(it) }
                    if (it == Unauthorized.description) authCallback()
                }
            )
        }
    }

    override fun onDestroy() = scope.cancel()
}