package component.main

import com.arkivanov.decompose.ComponentContext

class DefaultMainComponent(
    componentContext: ComponentContext,
    override val server: String
) : MainComponent, ComponentContext by componentContext {
    override val title: String = "Friends"
}