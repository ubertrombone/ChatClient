package component.main.add

import api.ApplicationApi
import db.ChatRepository
import settings.SettingsRepository

interface AddComponent {
    val server: ApplicationApi
    val chatRepository: ChatRepository
    val settings: SettingsRepository
}