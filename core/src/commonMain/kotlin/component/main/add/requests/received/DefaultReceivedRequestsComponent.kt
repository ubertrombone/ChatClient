package component.main.add.requests.received

import api.ApplicationApi
import api.model.FriendRequest
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.add.model.Requests
import util.Status
import util.Status.Loading

class DefaultReceivedRequestsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val receiveListModel: ReceiveListModel,
    override val navToSent: () -> Unit,
    override val dismiss: () -> Unit,
    override val logout: () -> Unit
) : ReceivedRequestsComponent, ComponentContext by componentContext {
    override val listStatus: Value<Status> = receiveListModel.status
    override val listLoading: Value<Boolean> = receiveListModel.loadingState
    override val receivedList: Value<Requests> = receiveListModel.result

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

    override fun getRequests() = receiveListModel.getRequests()

    override fun acceptRequest(request: FriendRequest) = _acceptRequestStates.addFriend(request)

    override fun rejectRequest(request: FriendRequest) = _acceptRequestStates.closeRequest(request)
}