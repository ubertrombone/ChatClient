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
    //  1. Upon sending/receiving message, check if Chat record exists
    //  2. If not, create a Chat record
    //  3. Sending a message:
    //      - Send the message to server
    //      - If server response is successful: Add Message record (collect timestamp from response, moment of send, or moment of response?)
    //      - If failure, create a Message record with some kind of error appended to be displayed in UI.
    //          -- Failed sends should only be displayed to the sender and should not be stored in server cache or erased on server alignment
    //  4. Receiving a message:
    //      - Write message to Messages
    //  5. UI should be collecting messages from DB

    init {
        scope.launch {
            chat = chatRepository.selectChat(userOne = username.name, userTwo = friend.username.name)

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
        message = if (response.successful) response.message.message else "ERROR ${response.message.message}",
        sender = response.message.sender.name,
        timestamp = Clock.System.now(),
        primaryUserRef = username.name,
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