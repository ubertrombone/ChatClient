package component.main.add

import api.ApplicationApi
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import component.main.add.block.BlockComponent
import component.main.add.model.Friends
import component.main.add.requests.RequestComponent
import component.main.add.requests.received.ReceiveListModel
import kotlinx.serialization.Serializable
import util.Status
import util.Username

interface AddComponent {
    val server: ApplicationApi
    val receiveListModel: ReceiveListModel
    val logout: () -> Unit

    val slots: Value<ChildSlot<*, Slots>>

    fun dismissSlot()
    fun showSlot(config: Config)

    val queryLoadingState: Value<Boolean>
    val queryStatus: Value<Status>
    val query: Value<Friends>
    val queryInput: Value<String>

    val actionLoadingState: Value<Boolean>
    val actionStatus: Value<Status>

    fun searchUsers(query: String)
    fun sendFriendRequest(to: Username)
    fun addFriend(username: Username)
    fun updateInput(input: String)

    @Serializable
    sealed class Config {
        @Serializable data object BlockConfig : Config()
        @Serializable data object RequestConfig : Config()
    }

    sealed class Slots {
        class BlockList(val component: BlockComponent) : Slots()
        class Requests(val component: RequestComponent) : Slots()
    }
}