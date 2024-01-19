package component.main.group

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import db.ChatRepository

class DefaultGroupComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val chatRepository: ChatRepository
) : GroupComponent, ComponentContext by componentContext