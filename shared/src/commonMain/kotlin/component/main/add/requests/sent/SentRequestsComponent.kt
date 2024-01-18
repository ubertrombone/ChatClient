package component.main.add.requests.sent

import api.ApplicationApi
import com.arkivanov.decompose.value.Value
import component.main.add.model.Requests
import util.Status
import util.Username

interface SentRequestsComponent {
    val server: ApplicationApi
    val navBack: () -> Unit
    val dismiss: () -> Unit
    val logout: () -> Unit

    val listStatus: Value<Status>
    val listLoading: Value<Boolean>
    val sentList: Value<Requests>

    val actionStatus: Value<Status>
    val actionLoading: Value<Boolean>

    fun getSentList()
    fun cancelRequest(username: Username)
}