package component.main

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import util.Username

interface MainComponent {
    val title: String
    val server: String //TODO: This should be a custom server class
    
    val childStack: Value<ChildStack<*, Child>>
    
    fun onChatsTabClicked()
    fun onGroupChatsTabClicked()
    fun onSettingsTabClicked()
    fun onLogoutClicked()
    
    sealed class Child {
        class ChatsChild() : Child()
        class GroupChatsChild() : Child()
        class SettingsChild() : Child()
    }
}