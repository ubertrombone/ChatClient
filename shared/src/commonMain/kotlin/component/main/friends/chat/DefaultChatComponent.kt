package component.main.friends.chat

import api.ApplicationApi
import api.model.FriendInfo
import com.arkivanov.decompose.ComponentContext
import db.ChatRepository

class DefaultChatComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val chatRepository: ChatRepository,
    override val cache: Boolean,
    override val friend: FriendInfo
) : ChatComponent, ComponentContext by componentContext