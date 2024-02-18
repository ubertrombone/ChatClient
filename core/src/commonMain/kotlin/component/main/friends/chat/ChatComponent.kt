package component.main.friends.chat

import api.ApplicationApi
import api.ChatRequestApi
import api.WebSocketApi
import api.model.FriendInfo
import com.arkivanov.decompose.value.Value
import com.ubertrombone.chat.Messages
import db.ChatRepository
import kotlinx.coroutines.flow.SharedFlow
import util.Username

interface ChatComponent {
    val server: ApplicationApi
    val webSocket: WebSocketApi
    val chatRequests: ChatRequestApi
    val chatRepository: ChatRepository
    val cache: Boolean
    val username: Username
    val friend: FriendInfo
    val navBack: () -> Unit

    val messages: SharedFlow<List<Messages>>
    val userInput: Value<String>

    fun sendMessage()
    fun send(message: String)
    fun updateInput(text: String)
    fun clearInput()

    // TODO: Enter or click 'send' sends message to WS endpoint
    // TODO: User can delete or edit a message
}