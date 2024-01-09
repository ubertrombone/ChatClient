package component.main

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import component.main.DefaultMainComponent.Config.*
import component.main.MainComponent.Child
import component.main.MainComponent.Child.*
import component.main.add.AddComponent
import component.main.add.DefaultAddComponent
import component.main.friends.DefaultFriendsComponent
import component.main.friends.FriendsComponent
import component.main.group.DefaultGroupComponent
import component.main.group.GroupComponent
import component.main.settings.DefaultSettingsComponent
import component.main.settings.SettingsComponent
import db.ChatRepository
import io.ktor.client.statement.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import settings.SettingsRepository
import util.Constants.UNKNOWN_ERROR
import util.Status
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

    private val _isLogoutLoading = MutableValue(false)
    override val isLogoutLoading: Value<Boolean> = _isLogoutLoading

    private val _logoutStatus: MutableValue<Status> = MutableValue(Success)
    override val logoutStatus: Value<Status> = _logoutStatus

    private val _isInitLoading = MutableValue(true)
    override val isInitLoading: Value<Boolean> = _isInitLoading

    private val _initStatus: MutableValue<Status> = MutableValue(Loading)
    override val initStatus: Value<Status> = _initStatus

    private val navigation = StackNavigation<Config>()
    private val _childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Friends,
        handleBackButton = false,
        childFactory = ::createChild
    )
    override val childStack: Value<ChildStack<*, Child>> = _childStack

    private val settingsNavigation = SlotNavigation<SettingsConfig>()
    private val _settingsSlot =
        childSlot(
            source = settingsNavigation,
            serializer = SettingsConfig.serializer(),
            handleBackButton = true
        ) { _, childComponentContext ->
            DefaultSettingsComponent(
                componentContext = childComponentContext,
                server = server,
                settings = settings,
                onDismissed = settingsNavigation::dismiss,
                logout = {
                    settingsNavigation.dismiss()
                    onLogoutClicked()
                }
            )
        }
    override val settingsSlot: Value<ChildSlot<*, SettingsComponent>> = _settingsSlot

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): Child = when (config) {
        Friends -> FriendsChild(friends(componentContext))
        Group -> GroupChild(group(componentContext))
        Add -> AddChild(addComponent(componentContext))
    }

    private fun friends(componentContext: ComponentContext): FriendsComponent =
        DefaultFriendsComponent(
            componentContext = componentContext,
            server = server,
            chatRepository = chatRepository,
            cache = settings.cache.get().toBooleanStrict(),
            logout = onLogoutClicked
        )

    private fun group(componentContext: ComponentContext): GroupComponent =
        DefaultGroupComponent(
            componentContext = componentContext,
            server = server,
            chatRepository = chatRepository
        )

    private fun addComponent(componentContext: ComponentContext): AddComponent =
        DefaultAddComponent(
            componentContext = componentContext,
            server = server,
            settings = settings,
            logout = onLogoutClicked
        )

    override fun onFriendsTabClicked() = navigation.bringToFront(Friends)
    override fun onGroupChatsTabClicked() = navigation.bringToFront(Group)
    override fun onAddTabClicked() = navigation.bringToFront(Add)
    override fun showSettings() { settingsNavigation.activate(SettingsConfig) }
    override fun dismissSettings() { settingsNavigation.dismiss() }

    override fun logout() {
        scope.launch {
            callWrapper(
                isLoading = _isLogoutLoading,
                operation = { server.logout() },
                onSuccess = {
                    if (it is Error) {
                        _logoutStatus.update { _ -> it }
                        runCatching { it.body as? HttpResponse }.getOrNull()?.let { onLogoutClicked() }
                    }
                    if (it == Success) {
                        _logoutStatus.update { _ -> it }
                        settings.token.remove()
                        onLogoutClicked()
                    }
                },
                onError = { _logoutStatus.update { _ -> Error(UNKNOWN_ERROR) } }
            )
        }
    }

    @Serializable
    private sealed class Config {
        @Serializable data object Friends : Config()
        @Serializable data object Group : Config()
        @Serializable data object Add : Config()
    }

    @Serializable
    private data object SettingsConfig
}