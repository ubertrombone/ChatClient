package component.main.friends

import api.ApplicationApi
import api.ChatApi
import api.ChatRequestApi
import api.WebSocketApi
import api.model.FriendInfo
import api.model.OpenChatRequest
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import component.main.friends.chat.ChatComponent
import component.main.friends.chat.DefaultChatComponent
import component.main.friends.model.FriendsSet
import db.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import settings.SettingsRepository
import util.Status
import util.toUsername

class DefaultFriendsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val webSocket: WebSocketApi,
    override val chatRequests: ChatRequestApi,
    override val chatRepository: ChatRepository,
    override val settings: SettingsRepository,
    override val friendsModel: FriendsModelImpl,
    override val logout: () -> Unit
) : FriendsComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.IO)
    override val friends: Value<FriendsSet> = friendsModel.friendsListState
    override val isLoading: Value<Boolean> = friendsModel.friendsListLoading
    override val status: Value<Status> = friendsModel.friendsListStatus

    // TODO: collect all ChatAPI instances here
    private val chats = LinkedHashSet<ChatApi>()

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
//                webSocket = webSocket,
//                chatRequests = chatRequests,
                chatRepository = chatRepository,
                username = settings.username.get().toUsername(),
                cache = settings.cache.get().toBooleanStrict(),
                friend = config.user,
                navBack = ::dismissChat
            )
        }
    override val chatSlot: Value<ChildSlot<*, ChatComponent>> = _chatSlot

    override fun showChat(user: FriendInfo) = chatNavigation.activate(ChatConfig(user)).also {
        scope.launch {
            chatRequests.emitRequest(OpenChatRequest(
                sender = settings.username.get().toUsername(),
                recipient = user.username
            ))
        }
    }
    override fun dismissChat() = chatNavigation.dismiss()

    init {
        scope.launch {
            chatRequests.chats.collect(chats::add)
        }
    }

    @Serializable
    private data class ChatConfig(val user: FriendInfo)
}