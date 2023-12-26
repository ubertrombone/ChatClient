package component.root

import api.ApplicationApiImpl
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import component.login.DefaultLoginComponent
import component.login.LoginComponent
import component.main.DefaultMainComponent
import component.main.MainComponent
import component.register.DefaultRegisterComponent
import component.register.RegisterComponent
import component.root.DefaultRootComponent.Config.*
import component.root.RootComponent.Child.*
import db.ChatRepository
import db.DriverFactory
import db.createDatabase
import kotlinx.serialization.Serializable
import settings.SettingsRepository
import util.MainPhases.*

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val settingsRepository: SettingsRepository
) : RootComponent, ComponentContext by componentContext {
    private val database = createDatabase(DriverFactory())
    override val chatRepository = ChatRepository(database)
    override val server = ApplicationApiImpl(settingsRepository)
    private val navigation = StackNavigation<Config>()
    private val _childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Login,
        handleBackButton = true,
        childFactory = ::createChild
    )
    override val childStack: Value<ChildStack<*, RootComponent.Child>> = _childStack

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Login -> LoginChild(login(componentContext))
        is Main -> MainChild(main(componentContext))
        is Register -> RegisterChild(register(componentContext))
    }

    private fun login(componentContext: ComponentContext): LoginComponent =
        DefaultLoginComponent(
            componentContext = componentContext,
            settings = settingsRepository,
            server = server,
            pushTo = {
                when (it) {
                    LOGIN -> {}
                    REGISTER -> navigation.push(Register)
                    MAIN -> navigation.replaceAll(Main)
                }
            }
        )

    private fun register(componentContext: ComponentContext): RegisterComponent =
        DefaultRegisterComponent(
            componentContext = componentContext,
            settings = settingsRepository,
            server = server,
            pushTo = {
                when (it) {
                    LOGIN -> navigation.pop()
                    REGISTER -> {}
                    MAIN -> navigation.replaceAll(Main)
                }
            }
        )

    private fun main(componentContext: ComponentContext): MainComponent =
        DefaultMainComponent(
            componentContext = componentContext,
            chatRepository = chatRepository,
            server = server,
            settings = settingsRepository,
            onLogoutClicked = { navigation.replaceAll(Login) }
        )

    override fun onBackPressed() = navigation.pop()

    @Serializable
    private sealed class Config {
        @Serializable data object Login : Config()
        @Serializable data object Register : Config()
        @Serializable data object Main : Config()
    }
}