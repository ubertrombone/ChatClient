package component.main

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import db.ChatRepository

class DefaultMainComponent(
    componentContext: ComponentContext,
    override val chatRepository: ChatRepository,
    override val server: ApplicationApi
) : MainComponent, ComponentContext by componentContext {
    override val title: String = "Friends"
}