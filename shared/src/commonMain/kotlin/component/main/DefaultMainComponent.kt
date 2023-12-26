package component.main

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import component.main.DefaultMainComponent.Config.*
import component.main.MainComponent.Child
import component.main.MainComponent.Child.*
import component.main.chat.ChatComponent
import component.main.chat.DefaultChatComponent
import component.main.group.DefaultGroupComponent
import component.main.group.GroupComponent
import component.main.settings.DefaultSettingsComponent
import component.main.settings.SettingsComponent
import db.ChatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import settings.SettingsRepository
import util.Status.*

class DefaultMainComponent(
    componentContext: ComponentContext,
    override val chatRepository: ChatRepository,
    override val server: ApplicationApi,
    override val settings: SettingsRepository,
    override val onLogoutClicked: () -> Unit
) : MainComponent, ComponentContext by componentContext {
    private val scope = CoroutineScope(Dispatchers.IO)
    override val title: String = "Friends"

    private val _isLoading = MutableValue(false)
    override val isLoading: Value<Boolean> = _isLoading

    private val navigation = StackNavigation<Config>()
    private val _childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Chat,
        handleBackButton = false,
        childFactory = ::createChild
    )
    override val childStack: Value<ChildStack<*, Child>> = _childStack

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): Child = when (config) {
        Chat -> ChatChild(chat(componentContext))
        Group -> GroupChild(group(componentContext))
        Settings -> SettingsChild(settings(componentContext))
    }

    private fun chat(componentContext: ComponentContext): ChatComponent =
        DefaultChatComponent(
            componentContext = componentContext,
            server = server,
            chatRepository = chatRepository,
            cache = settings.cache.get().toBooleanStrict()
        )

    private fun group(componentContext: ComponentContext): GroupComponent =
        DefaultGroupComponent(
            componentContext = componentContext,
            server = server,
            chatRepository = chatRepository
        )

    private fun settings(componentContext: ComponentContext): SettingsComponent =
        DefaultSettingsComponent(
            componentContext = componentContext,
            server = server,
            settings = settings
        )

    override fun onChatsTabClicked() = navigation.bringToFront(Chat)
    override fun onGroupChatsTabClicked() = navigation.bringToFront(Group)
    override fun onSettingsTabClicked() = navigation.bringToFront(Settings)

    override fun logout() {
        scope.launch {
            callWrapper(
                isLoading = _isLoading,
                operation = { server.logout() },
                onSuccess = {
                    if (it is Error) println(it.message) // TODO: Handle this error in a snackbar?
                    if (it == Success) {
                        settings.token.remove()
                        onLogoutClicked()
                    }
                },
                onError = {} //TODO
            )
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable data object Chat : Config()
        @Serializable data object Group : Config()
        @Serializable data object Settings : Config()
    }
}