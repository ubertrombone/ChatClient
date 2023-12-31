package component.main.friends

import api.ApplicationApi
import api.callWrapper
import api.model.FriendInfo
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.main.friends.chat.ChatComponent
import component.main.friends.chat.DefaultChatComponent
import component.main.friends.model.FriendsSet
import db.ChatRepository
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import util.Constants.UNKNOWN_ERROR
import util.Status
import util.Status.*

class DefaultFriendsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val chatRepository: ChatRepository,
    override val cache: Boolean
) : FriendsComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.Main)

    private val _friends: MutableValue<FriendsSet> = MutableValue(
        stateKeeper.consume(
            key = FRIENDS_LIST_STATE,
            strategy = FriendsSet.serializer()
        ) ?: FriendsSet()
    )
    override val friends: Value<FriendsSet> = _friends

    private val _isLoading = MutableValue(true)
    override val isLoading: Value<Boolean> = _isLoading

    private val _status: MutableValue<Status> = MutableValue(Loading)
    override val status: Value<Status> = _status

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

    override fun getFriends() {
        scope.launch {
            callWrapper(
                isLoading = _isLoading,
                operation = { server.getFriends() },
                onSuccess = { friends ->
                    println("FRIENDS $friends")
                    friends?.let {
                        _friends.update { FriendsSet(friends = friends.toImmutableSet()) }
                        _status.update { Success }
                    } ?: Error(UNKNOWN_ERROR)
                },
                onError = { Error(it) }
            )
        }
    }

    init {
        stateKeeper.register(key = FRIENDS_LIST_STATE, strategy = FriendsSet.serializer()) { _friends.value }
    }

    @Serializable
    private data class ChatConfig(val user: FriendInfo)

    private companion object {
        const val FRIENDS_LIST_STATE = "FRIENDS_LIST_STATE"
    }
}