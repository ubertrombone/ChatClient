package component.main.friends.chat

import api.ApplicationApi
import api.WebSocketApi
import api.model.ChatMessage
import api.model.FriendInfo
import com.ubertrombone.chat.Messages
import db.ChatRepository
import kotlinx.coroutines.flow.SharedFlow
import util.Username

interface ChatComponent {
    val server: ApplicationApi
    val webSocket: WebSocketApi
    val chatRepository: ChatRepository
    val cache: Boolean
    val username: Username
    val friend: FriendInfo

    val messages: SharedFlow<List<Messages>>
    val incomingMessages: SharedFlow<ChatMessage>

    fun sendMessage()
    fun send(message: String)

    // TODO: Enter or click 'send' sends message to WS endpoint
    // TODO: User can delete or edit a message
}