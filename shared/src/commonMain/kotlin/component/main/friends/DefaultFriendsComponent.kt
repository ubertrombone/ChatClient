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
import db.ChatRepository
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import util.Constants.UNKNOWN_ERROR
import util.Status
import util.Status.*
import util.Username

@OptIn(InternalSerializationApi::class)
class DefaultFriendsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val chatRepository: ChatRepository,
    override val cache: Boolean
) : FriendsComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.IO)

    @Suppress("UNCHECKED_CAST")
    private val _friends: MutableValue<ImmutableSet<FriendInfo>> = MutableValue(
        initialValue = stateKeeper.consume(
            key = FRIENDS_LIST_STATE,
            strategy = ImmutableSet::class.serializer()
        ) as? ImmutableSet<FriendInfo> ?: persistentSetOf()
    )
    override val friends: Value<ImmutableSet<FriendInfo>> = _friends

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
        ) { _, childComponentContext ->
            DefaultChatComponent(
                componentContext = childComponentContext,
                server = server,
                chatRepository = chatRepository,
                cache = cache
            )
        }
    override val chatSlot: Value<ChildSlot<*, ChatComponent>> = _chatSlot

    override fun showChat(username: Username) { chatNavigation.activate(ChatConfig(username)) }
    override fun dismissChat() { chatNavigation.dismiss() }

    override fun getFriends() {
        scope.launch {
            callWrapper(
                isLoading = _isLoading,
                operation = { server.getFriends() },
                onSuccess = { friends ->
                    friends?.let {
                        _friends.update { it }
                        _status.update { Success }
                    } ?: Error(UNKNOWN_ERROR)
                },
                onError = { Error(it) }
            )
        }
    }

    init {
        stateKeeper.register(
            key = FRIENDS_LIST_STATE,
            strategy = ImmutableSet::class.serializer()
        ) { _friends.value }
    }

    @Serializable
    private data class ChatConfig(val username: Username)

    private companion object {
        const val FRIENDS_LIST_STATE = "FRIENDS_LIST_STATE"
    }
}