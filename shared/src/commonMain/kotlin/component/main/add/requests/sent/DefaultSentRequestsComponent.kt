package component.main.add.requests.sent

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.add.model.Requests
import kotlinx.serialization.builtins.serializer
import util.Status
import util.Status.Loading
import util.Status.Success
import util.Username

class DefaultSentRequestsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val navBack: () -> Unit,
    override val dismiss: () -> Unit,
    override val logout: () -> Unit
) : SentRequestsComponent, ComponentContext by componentContext {
    private val _getSentRequestStates = instanceKeeper.getOrCreate(SENT_KEY) {
        SentListModel(
            initialLoadingState = stateKeeper.consume(key = LOADING_KEY, strategy = Boolean.serializer()) ?: true,
            initialStatus = stateKeeper.consume(key = STATUS_KEY, strategy = Status.serializer()) ?: Loading,
            initialState = stateKeeper.consume(key = SENT_KEY, strategy = Requests.serializer()) ?: Requests(),
            server = server,
            authCallback = logout
        )
    }
    override val listStatus: Value<Status> = _getSentRequestStates.status
    override val listLoading: Value<Boolean> = _getSentRequestStates.loadingState
    override val sentList: Value<Requests> = _getSentRequestStates.result

    private val _cancelRequestStates = instanceKeeper.getOrCreate {
        CancelModel(
            initialLoadingState = false,
            initialStatus = Success,
            server = server,
            authCallback = logout
        )
    }
    override val actionStatus: Value<Status> = _cancelRequestStates.status
    override val actionLoading: Value<Boolean> = _cancelRequestStates.loadingState

    override fun getSentList() = _getSentRequestStates.getSentRequests()

    override fun cancelRequest(username: Username) = _cancelRequestStates.cancelRequest(username)

    init {
        stateKeeper.register(key = SENT_KEY, strategy = Requests.serializer()) { _getSentRequestStates.result.value }
        stateKeeper.register(key = LOADING_KEY, strategy = Boolean.serializer()) { _getSentRequestStates.loadingState.value }
        stateKeeper.register(key = STATUS_KEY, strategy = Status.serializer()) { _getSentRequestStates.status.value }
    }

    private companion object {
        const val SENT_KEY = "SENT_KEY"
        const val LOADING_KEY = "LOADING_KEY"
        const val STATUS_KEY = "STATUS_KEY"
    }
}