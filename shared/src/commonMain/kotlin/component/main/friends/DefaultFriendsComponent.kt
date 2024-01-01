package component.main.friends

import api.ApplicationApi
import api.model.FriendInfo
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.friends.chat.ChatComponent
import component.main.friends.chat.DefaultChatComponent
import component.main.friends.model.FriendsSet
import db.ChatRepository
import kotlinx.serialization.Serializable
import util.Status
import util.Status.Loading

class DefaultFriendsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val chatRepository: ChatRepository,
    override val cache: Boolean
) : FriendsComponent, ComponentContext by componentContext {
    private val _friends = instanceKeeper.getOrCreate(FRIENDS_LIST_STATE) {
        FriendsModelImpl(
            initialState = stateKeeper.consume(FRIENDS_LIST_STATE, strategy = FriendsSet.serializer()) ?: FriendsSet(),
            initialLoadingState = true,
            initialStatus = Loading,
            server = server
        )
    }
    override val friends: Value<FriendsSet> = _friends.friendsListState
    override val isLoading: Value<Boolean> = _friends.friendsListLoading
    override val status: Value<Status> = _friends.friendsListStatus

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

    init {
        stateKeeper.register(
            key = FRIENDS_LIST_STATE,
            strategy = FriendsSet.serializer()
        ) { _friends.friendsListState.value }
    }

    @Serializable
    private data class ChatConfig(val user: FriendInfo)

    private companion object {
        const val FRIENDS_LIST_STATE = "FRIENDS_LIST_STATE"
    }
}