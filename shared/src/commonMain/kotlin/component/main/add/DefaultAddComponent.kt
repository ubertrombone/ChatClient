package component.main.add

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import db.ChatRepository
import settings.SettingsRepository

class DefaultAddComponent(
    componentContext: ComponentContext,
    override val chatRepository: ChatRepository,
    override val server: ApplicationApi,
    override val settings: SettingsRepository
) : AddComponent, ComponentContext by componentContext {
}