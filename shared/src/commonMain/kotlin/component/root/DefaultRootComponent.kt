package component.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import component.login.DefaultLoginComponent
import component.login.LoginComponent
import component.main.DefaultMainComponent
import component.main.MainComponent
import component.register.DefaultRegisterComponent
import component.register.RegisterComponent
import component.root.RootComponent.Child.*
import db.ChatRepository
import db.DriverFactory
import db.createDatabase
import kotlinx.serialization.Serializable
import settings.SettingsRepository
import util.MainPhases

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val settingsRepository: SettingsRepository
) : RootComponent, ComponentContext by componentContext {
    private val database = createDatabase(DriverFactory())
    override val chatRepository = ChatRepository(database)

    private val navigation = StackNavigation<Config>()

    private val _childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Login,
        handleBackButton = false,
        childFactory = ::createChild
    )
    override val childStack: Value<ChildStack<*, RootComponent.Child>> = _childStack

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child = when (config) {
        is Config.Login -> LoginChild(login(componentContext))
        is Config.Main -> MainChild(main(componentContext))
        is Config.Register -> RegisterChild(register(componentContext))
    }

    private fun login(componentContext: ComponentContext): LoginComponent =
        DefaultLoginComponent(
            componentContext = componentContext,
            token = settingsRepository.token.get(),
            server = "SERVER",
            pushTo = {
                when (it) {
                    MainPhases.LOGIN -> {}
                    MainPhases.REGISTER -> navigation.push(Config.Register)
                    MainPhases.MAIN -> navigation.push(Config.Main)
                }
            }
        )

    private fun register(componentContext: ComponentContext): RegisterComponent =
        DefaultRegisterComponent(
            componentContext = componentContext,
            server = "SERVER",
            pushTo = {
                when (it) {
                    MainPhases.LOGIN -> navigation.pop()
                    MainPhases.REGISTER -> {}
                    MainPhases.MAIN -> navigation.push(Config.Main)
                }
            }
        )

    private fun main(componentContext: ComponentContext): MainComponent =
        DefaultMainComponent(
            componentContext = componentContext,
            chatRepository = chatRepository,
            server = "SERVER"
        )

    override fun onBackPressed() = navigation.pop()

    @Serializable
    private sealed class Config {
        @Serializable data object Login : Config()
        @Serializable data object Register : Config()
        @Serializable data object Main : Config()
    }
}