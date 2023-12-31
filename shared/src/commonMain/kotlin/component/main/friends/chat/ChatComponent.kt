package component.main.friends.chat

import api.ApplicationApi
import db.ChatRepository

interface ChatComponent {
    val server: ApplicationApi
    val chatRepository: ChatRepository
    val cache: Boolean
}