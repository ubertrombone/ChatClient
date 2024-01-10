package component.main.add.requests.received

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import util.Status
import util.Username

class AcceptModel(
    initialLoadingState: Boolean,
    initialStatus: Status,
    private val server: ApplicationApi,
    private val authCallback: () -> Unit
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(Dispatchers.Main)
    val loadingState = MutableValue(initialLoadingState)
    val status = MutableValue(initialStatus)

    fun receiveRequest(username: Username, accept: Boolean) {
        scope.launch {
            callWrapper(
                isLoading = loadingState,
                operation = {  },
                onSuccess = { status.update { _ -> it } },
                onError = {
                    status.update { _ -> Status.Error(it) }
                    if (it == HttpStatusCode.Unauthorized.description) authCallback()
                }
            )
        }
    }

    override fun onDestroy() = scope.cancel()
}