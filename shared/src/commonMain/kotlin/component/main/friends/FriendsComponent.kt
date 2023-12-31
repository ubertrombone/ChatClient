package component.main.friends

import api.ApplicationApi
import db.ChatRepository

interface FriendsComponent {
    val server: ApplicationApi
    val chatRepository: ChatRepository
    val cache: Boolean


}