package component.main.add

import api.ApplicationApi
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import component.main.add.block.BlockComponent
import component.main.add.model.Friends
import component.main.add.requests.RequestComponent
import settings.SettingsRepository
import util.Status
import util.Username

interface AddComponent {
    val server: ApplicationApi
    val settings: SettingsRepository
    val logout: () -> Unit

    val blockSlot: Value<ChildSlot<*, BlockComponent>>
    fun showBlock()
    fun dismissBlock()

    val requestSlot: Value<ChildSlot<*, RequestComponent>>
    fun showRequest()
    fun dismissRequest()

    val friendsLoadingState: Value<Boolean>
    val friendsStatus: Value<Status>
    val friends: Value<Friends>

    val queryLoadingState: Value<Boolean>
    val queryStatus: Value<Status>
    val query: Value<Friends>

    val actionLoadingState: Value<Boolean>
    val actionStatus: Value<Status>

    fun getFriends()
    fun searchUsers(query: String)
    fun sendFriendRequest(to: Username)
    fun addFriend(username: Username) // to be called if friend request already exists
    fun removeFriend(username: Username)
}