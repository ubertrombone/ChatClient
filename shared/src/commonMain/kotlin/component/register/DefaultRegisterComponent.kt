package component.register

import com.arkivanov.decompose.ComponentContext
import util.MainPhases

class DefaultRegisterComponent(
    componentContext: ComponentContext,
    override val server: String,
    override val pushTo: (MainPhases) -> Unit
) : RegisterComponent, ComponentContext by componentContext {
    override val title: String = "Register"
}