package component.login

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import util.MainPhases

class DefaultLoginComponent(
    componentContext: ComponentContext,
    override val token: String,
    override val server: ApplicationApi,
    override val pushTo: (MainPhases) -> Unit
) : LoginComponent, ComponentContext by componentContext {
    override val title: String = "Login"
}