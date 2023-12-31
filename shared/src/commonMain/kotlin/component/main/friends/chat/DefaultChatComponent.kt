package component.main.friends.chat

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import db.ChatRepository

class DefaultChatComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val chatRepository: ChatRepository,
    override val cache: Boolean
) : ChatComponent, ComponentContext by componentContext