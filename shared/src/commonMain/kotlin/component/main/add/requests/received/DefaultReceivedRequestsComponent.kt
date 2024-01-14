package component.main.add.requests.received

import androidx.compose.material3.SnackbarHostState
import api.ApplicationApi
import api.model.FriendRequest
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.add.model.Requests
import kotlinx.serialization.builtins.serializer
import util.Status
import util.Status.Loading

class DefaultReceivedRequestsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val navToSent: () -> Unit,
    override val dismiss: () -> Unit,
    override val logout: () -> Unit
) : ReceivedRequestsComponent, ComponentContext by componentContext {
    override val snackbarHostState = SnackbarHostState()

    private val _getRequestStates = instanceKeeper.getOrCreate(RECEIVED_KEY) {
        ReceiveListModel(
            initialLoadingState = stateKeeper.consume(key = LOADING_KEY, strategy = Boolean.serializer()) ?: true,
            initialStatus = stateKeeper.consume(key = STATUS_KEY, strategy = Status.serializer()) ?: Loading,
            initialState = stateKeeper.consume(key = RECEIVED_KEY, strategy = Requests.serializer()) ?: Requests(),
            server = server,
            authCallback = logout
        )
    }
    override val listStatus: Value<Status> = _getRequestStates.status
    override val listLoading: Value<Boolean> = _getRequestStates.loadingState
    override val receivedList: Value<Requests> = _getRequestStates.result

    private val _acceptRequestStates = instanceKeeper.getOrCreate {
        AcceptModel(
            initialLoadingState = false,
            initialStatus = Loading,
            server = server,
            authCallback = logout
        )
    }
    override val actionStatus: Value<Status> = _acceptRequestStates.status
    override val actionLoading: Value<Boolean> = _acceptRequestStates.loadingState

    override fun getRequests() = _getRequestStates.getRequests()

    override fun acceptRequest(request: FriendRequest) = _acceptRequestStates.addFriend(request)

    override fun rejectRequest(request: FriendRequest) = _acceptRequestStates.closeRequest(request)

    init {
        stateKeeper.register(key = RECEIVED_KEY, strategy = Requests.serializer()) { _getRequestStates.result.value }
        stateKeeper.register(key = LOADING_KEY, strategy = Boolean.serializer()) { _getRequestStates.loadingState.value }
        stateKeeper.register(key = STATUS_KEY, strategy = Status.serializer()) { _getRequestStates.status.value }
    }

    private companion object {
        const val RECEIVED_KEY = "RECEIVED_KEY"
        const val LOADING_KEY = "LOADING_KEY"
        const val STATUS_KEY = "STATUS_KEY"
    }
}