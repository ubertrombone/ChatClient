package component.main.settings.warning

import kotlin.coroutines.CoroutineContext

interface DeleteAccountComponent {
    val deleteAccount: suspend (CoroutineContext) -> Unit
    val dismiss: () -> Unit
}