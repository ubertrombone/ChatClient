package component.main.friends.chat

import api.ApplicationApi
import api.WebSocketApi
import api.model.ChatMessage
import api.model.FriendInfo
import api.model.SendChatResponse
import com.arkivanov.decompose.ComponentContext
import com.ubertrombone.chat.Chats
import db.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import model.Message
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
    //  3/4a. On server, if User.cache is true, store non-error messages
    //  5. UI should be collecting messages from DB

    init {
        scope.launch {
            chat = chatRepository.selectChat(userOne = username.name, userTwo = friend.username.name).also {
                if (it.first() == null) chatRepository.insertChat(userOne = username.name, userTwo = friend.username.name)
            }

            launch { webSocket.incomingMessages.receiveMessages() }
            launch { webSocket.response.receiveResponses() }
        }
    }

    private suspend fun SharedFlow<ChatMessage>.receiveMessages(): Unit = collect { insertMessage(it) }
    private suspend fun SharedFlow<SendChatResponse>.receiveResponses(): Unit = collect { insertMessage(it) }

    private suspend fun <T> insertMessage(type: T) {
        chat.first()?.let {
            chatRepository.insertMessage(
                if (type is ChatMessage) insertChatMessage(type, it.id.toInt())
                else insertResponse(type as SendChatResponse, it.id.toInt())
            )
        }
    }

    private fun insertChatMessage(message: ChatMessage, chatId: Int) = Message(
        message = message.message,
        sender = message.sender.name,
        timestamp = Clock.System.now(),
        primaryUserRef = username.name,
        chat = chatId
    )

    private fun insertResponse(response: SendChatResponse, chatId: Int) = Message(
        message = response.message.message,
        sender = response.message.sender.name,
        timestamp = Clock.System.now(),
        primaryUserRef = username.name,
        error = if (response.successful) null else 1,
        chat = chatId
    )

    override val incomingMessages: SharedFlow<ChatMessage> = webSocket.incomingMessages

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