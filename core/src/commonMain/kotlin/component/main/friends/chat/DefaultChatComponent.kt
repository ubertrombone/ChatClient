package component.main.friends.chat

import api.ApplicationApi
import api.ChatRequestApi
import api.WebSocketApi
import api.model.FriendInfo
import api.model.OpenChatRequest
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.ubertrombone.chat.Chats
import com.ubertrombone.chat.Messages
import db.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import util.Username

class DefaultChatComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val webSocket: WebSocketApi,
    override val chatRequests: ChatRequestApi,
    override val chatRepository: ChatRepository,
    override val username: Username,
    override val cache: Boolean,
    override val friend: FriendInfo,
    override val navBack: () -> Unit
) : ChatComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.IO)
    private lateinit var chat: Flow<Chats?>

    private val _messages = MutableSharedFlow<List<Messages>>()
    override val messages: SharedFlow<List<Messages>> = _messages

    private val _userInput = MutableValue("")
    override val userInput: Value<String> = _userInput

    init {
        scope.launch {
//            chat = chatRepository.selectChat(userOne = username.name, userTwo = friend.username.name).also {
//                if (it.first() == null) chatRepository.insertChat(userOne = username.name, userTwo = friend.username.name)
//            }
//
//            launch {
//                chatRepository.getMessagesFromChat(chat.first()!!.id.toInt()).collect {
//                    _messages.emit(it)
//                }
//            }
        }
    }

    override fun send(message: String) {
        scope.launch {
//            webSocket.emitInput(
//                ChatMessage(
//                    sender = username,
//                    recipientOrGroup = friend.username.name,
//                    message = message
//                )
//            )

            // TODO: This should be done on init
            // TODO: after emitting request, collect existing messages (from Cache?)
            chatRequests.emitRequest(OpenChatRequest(
                sender = username,
                recipient = friend.username
            ))
        }
    }

    override fun updateInput(text: String) = _userInput.update { text }
    override fun clearInput() = _userInput.update { "" }

    override fun sendMessage() {
        scope.launch {
//            webSocket.emitInput(
//                ChatMessage(
//                    sender = username,
//                    recipientOrGroup = friend.username.name,
//                    message = "${username.name} sending message to ${friend.username.name}"
//                )
//            )
        }
    }
}