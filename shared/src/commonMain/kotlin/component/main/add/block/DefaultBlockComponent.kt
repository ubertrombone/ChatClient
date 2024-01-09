package component.main.add.block

import com.arkivanov.decompose.ComponentContext

class DefaultBlockComponent(
    componentContext: ComponentContext
) : BlockComponent, ComponentContext by componentContext