package component.main.friends

import api.ApplicationApi
import api.model.FriendInfo
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import component.main.friends.chat.ChatComponent
import db.ChatRepository
import kotlinx.collections.immutable.ImmutableSet
import util.Status
import util.Username

interface FriendsComponent {
    val server: ApplicationApi
    val chatRepository: ChatRepository
    val cache: Boolean

    val friends: Value<ImmutableSet<FriendInfo>>
    val isLoading: Value<Boolean>
    val status: Value<Status>
    val chatSlot: Value<ChildSlot<*, ChatComponent>>

    fun showChat(username: Username)
    fun dismissChat()
    fun getFriends()
}