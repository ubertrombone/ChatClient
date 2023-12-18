package component.main.group

import api.ApplicationApi
import db.ChatRepository

interface GroupComponent {
    val server: ApplicationApi
    val chatRepository: ChatRepository
}