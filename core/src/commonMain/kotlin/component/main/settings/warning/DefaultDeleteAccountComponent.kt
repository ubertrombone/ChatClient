package component.main.settings.warning

import com.arkivanov.decompose.ComponentContext
import kotlin.coroutines.CoroutineContext

class DefaultDeleteAccountComponent(
    componentContext: ComponentContext,
    override val deleteAccount: suspend (CoroutineContext) -> Unit,
    override val dismiss: () -> Unit
) : DeleteAccountComponent, ComponentContext by componentContext