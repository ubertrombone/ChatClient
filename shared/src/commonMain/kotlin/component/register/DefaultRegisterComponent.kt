package component.register

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import util.MainPhases

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val pushTo: (MainPhases) -> Unit
) : RegisterComponent, ComponentContext by componentContext {
    override val title: String = "Register"
}