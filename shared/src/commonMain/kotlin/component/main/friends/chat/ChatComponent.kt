package component.main.friends.chat

import api.ApplicationApi
import api.model.FriendInfo
import db.ChatRepository

interface ChatComponent {
    val server: ApplicationApi
    val chatRepository: ChatRepository
    val cache: Boolean
    val friend: FriendInfo
}