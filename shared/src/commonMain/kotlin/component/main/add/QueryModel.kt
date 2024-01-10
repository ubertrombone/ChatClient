package component.main.add

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.add.model.Friends
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.*
import util.Constants.UNKNOWN_ERROR
import util.Status
import util.Status.Error
import util.Status.Success

class QueryModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    initialState: Friends,
    private val server: ApplicationApi,
    private val authCallback: () -> Unit
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)

    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)
    val result = MutableValue(initialState)
    private var job: Job? = null

    fun apiCall(query: String) {
        job?.cancel()
        job = scope.launch {
            delay(500)
            callWrapper(
                isLoading = loadingState,
                operation = { server.queryUsers(query) },
                onSuccess = { results ->
                    results?.let { r ->
                        result.update { Friends(r.toImmutableSet()) }
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