package component.main.add.block

import api.ApplicationApi
import com.arkivanov.decompose.value.Value
import component.main.add.model.Friends
import util.Status
import util.Username

interface BlockComponent {
    val server: ApplicationApi
    val dismiss: () -> Unit
    val logout: () -> Unit

    val listStatus: Value<Status>
    val listLoading: Value<Boolean>
    val blockList: Value<Friends>

    val actionStatus: Value<Status>
    val actionLoading: Value<Boolean>

    fun getBlockList()
    fun unblock(user: Username)
}