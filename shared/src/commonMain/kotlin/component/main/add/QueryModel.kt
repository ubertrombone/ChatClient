package component.main.add

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.add.model.Friends
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import util.Constants.UNKNOWN_ERROR
import util.Status
import util.Status.Error
import util.Status.Success

class QueryModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    initialState: Friends,
    private val server: ApplicationApi,
    val authCallback: () -> Unit
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)

    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)
    val result = MutableValue(initialState)

    fun apiCall(query: String) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = { server.queryUsers(query) },
                onSuccess = { results ->
                    results?.let {
                        result.update { Friends(results.toImmutableSet()) }
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