package component.main.friends

import api.ApplicationApi
import api.model.FriendInfo
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import component.main.friends.chat.ChatComponent
import component.main.friends.chat.DefaultChatComponent
import component.main.friends.model.FriendsSet
import db.ChatRepository
import kotlinx.serialization.Serializable
import util.Status

class DefaultFriendsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val chatRepository: ChatRepository,
    override val cache: Boolean,
    override val friendsModel: FriendsModelImpl,
    override val logout: () -> Unit
) : FriendsComponent, ComponentContext by componentContext {
    override val friends: Value<FriendsSet> = friendsModel.friendsListState
    override val isLoading: Value<Boolean> = friendsModel.friendsListLoading
    override val status: Value<Status> = friendsModel.friendsListStatus

    // TODO: Have some kind of profile viewer here where a user will be able to block/remove a friend from their friends
    // TODO: Also consider making these actions possible within a FriendCard
    private val chatNavigation = SlotNavigation<ChatConfig>()
    private val _chatSlot =
        childSlot(
            source = chatNavigation,
            serializer = ChatConfig.serializer(),
            handleBackButton = true
        ) { config, childComponentContext ->
            DefaultChatComponent(
                componentContext = childComponentContext,
                server = server,
                chatRepository = chatRepository,
                cache = cache,
                friend = config.user
            )
        }
    override val chatSlot: Value<ChildSlot<*, ChatComponent>> = _chatSlot

    override fun showChat(user: FriendInfo) { chatNavigation.activate(ChatConfig(user)) }
    override fun dismissChat() { chatNavigation.dismiss() }

    @Serializable
    private data class ChatConfig(val user: FriendInfo)
}