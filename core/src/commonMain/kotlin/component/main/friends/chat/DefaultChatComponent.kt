package component.main.friends.chat

import api.ApplicationApi
import api.WebSocketApi
import api.model.ChatMessage
import api.model.FriendInfo
import com.arkivanov.decompose.ComponentContext
import com.ubertrombone.chat.Chats
import com.ubertrombone.chat.Messages
import db.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import util.Username

class DefaultChatComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val webSocket: WebSocketApi,
    override val chatRepository: ChatRepository,
    override val username: Username,
    override val cache: Boolean,
    override val friend: FriendInfo
) : ChatComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var chat: Flow<Chats?>
    // TODO:
    //  5. UI should be collecting messages from DB

    private val _messages = MutableSharedFlow<List<Messages>>()
    override val messages: SharedFlow<List<Messages>> = _messages

    init {
        scope.launch {
            chat = chatRepository.selectChat(userOne = username.name, userTwo = friend.username.name).also {
                if (it.first() == null) chatRepository.insertChat(userOne = username.name, userTwo = friend.username.name)
            }

            launch {
                chatRepository.getMessagesFromChat(chat.first()!!.id.toInt()).collect {
                    _messages.emit(it)
                }
            }
        }
    }

    override fun send(message: String) {
        scope.launch {
            webSocket.emitInput(
                ChatMessage(
                    sender = username,
                    recipientOrGroup = friend.username.name,
                    message = message
                )
            )
        }
    }

    override fun sendMessage() {
        scope.launch {
            webSocket.emitInput(
                ChatMessage(
                    sender = username,
                    recipientOrGroup = friend.username.name,
                    message = "${username.name} sending message to ${friend.username.name}"
                )
            )
        }
    }
}