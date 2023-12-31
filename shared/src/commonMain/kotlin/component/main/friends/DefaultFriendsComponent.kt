package component.main.friends

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import db.ChatRepository

class DefaultFriendsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val chatRepository: ChatRepository,
    override val cache: Boolean
) : FriendsComponent, ComponentContext by componentContext