package component.main

import api.ApplicationApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import component.main.chat.ChatComponent
import component.main.group.GroupComponent
import component.main.settings.SettingsComponent
import db.ChatRepository
import settings.SettingsRepository

interface MainComponent {
    val chatRepository: ChatRepository
    val server: ApplicationApi
    val settings: SettingsRepository
    val onLogoutClicked: () -> Unit

    val title: String
    val isLoading: Value<Boolean>
    val childStack: Value<ChildStack<*, Child>>
    
    fun onChatsTabClicked()
    fun onGroupChatsTabClicked()
    fun onSettingsTabClicked()

    fun logout()
    
    sealed class Child {
        class ChatChild(val component: ChatComponent) : Child()
        class GroupChild(val component: GroupComponent) : Child()
        class SettingsChild(val component: SettingsComponent) : Child()
    }
}