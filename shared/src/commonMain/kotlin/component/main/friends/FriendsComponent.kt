package component.main.friends

import api.ApplicationApi
import api.model.FriendInfo
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import component.main.friends.chat.ChatComponent
import component.main.friends.model.FriendsSet
import db.ChatRepository
import util.Status

interface FriendsComponent {
    val server: ApplicationApi
    val chatRepository: ChatRepository
    val cache: Boolean
    val friendsModel: FriendsModelImpl
    val logout: () -> Unit

    val friends: Value<FriendsSet>
    val isLoading: Value<Boolean>
    val status: Value<Status>
    val chatSlot: Value<ChildSlot<*, ChatComponent>>

    fun showChat(user: FriendInfo)
    fun dismissChat()
}