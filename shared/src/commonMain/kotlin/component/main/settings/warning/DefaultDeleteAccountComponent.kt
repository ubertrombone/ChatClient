package component.main.settings.warning

import com.arkivanov.decompose.ComponentContext

class DefaultDeleteAccountComponent(
    componentContext: ComponentContext,
    override val deleteAccount: () -> Unit,
    override val dismiss: () -> Unit
) : DeleteAccountComponent, ComponentContext by componentContext