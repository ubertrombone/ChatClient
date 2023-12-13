package component.login

import com.arkivanov.decompose.ComponentContext
import util.MainPhases

class DefaultLoginComponent(
    componentContext: ComponentContext,
    override val token: String,
    override val server: String,
    override val pushTo: (MainPhases) -> Unit
) : LoginComponent, ComponentContext by componentContext {
    override val title: String = "Login"
}