package component.main.add.requests.received

import api.ApplicationApi
import api.model.FriendRequest
import com.arkivanov.decompose.value.Value
import component.main.add.model.Requests
import util.Status

interface ReceivedRequestsComponent {
    val server: ApplicationApi
    val receiveListModel: ReceiveListModel
    val navToSent: () -> Unit
    val dismiss: () -> Unit
    val logout: () -> Unit

    val listStatus: Value<Status>
    val listLoading: Value<Boolean>
    val receivedList: Value<Requests>

    val actionStatus: Value<Status>
    val actionLoading: Value<Boolean>

    fun getRequests()
    fun acceptRequest(request: FriendRequest)
    fun rejectRequest(request: FriendRequest)
}