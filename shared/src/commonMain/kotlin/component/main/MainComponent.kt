package component.main

import api.ApplicationApi
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import component.main.add.AddComponent
import component.main.friends.FriendsComponent
import component.main.group.GroupComponent
import component.main.settings.SettingsComponent
import db.ChatRepository
import settings.SettingsRepository
import util.Status

interface MainComponent {
    val chatRepository: ChatRepository
    val server: ApplicationApi
    val settings: SettingsRepository
    val onLogoutClicked: () -> Unit

    val title: String
    val isLogoutLoading: Value<Boolean>
    val logoutStatus: Value<Status>
    val isInitLoading: Value<Boolean>
    val initStatus: Value<Status>
    val childStack: Value<ChildStack<*, Child>>
    val settingsSlot: Value<ChildSlot<*, SettingsComponent>>
    
    fun onFriendsTabClicked()
    fun onGroupChatsTabClicked()
    fun onAddTabClicked()
    fun showSettings()
    fun dismissSettings()

    fun logout()
    
    sealed class Child {
        class FriendsChild(val component: FriendsComponent) : Child()
        class GroupChild(val component: GroupComponent) : Child()
        class AddChild(val component: AddComponent) : Child()
    }
}