package component.main.add.requests

import com.arkivanov.decompose.ComponentContext

class DefaultRequestComponent(
    componentContext: ComponentContext
) : RequestComponent, ComponentContext by componentContext