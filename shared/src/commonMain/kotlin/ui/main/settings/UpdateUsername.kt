package ui.main.settings

import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import ui.composables.states.UsernameAuthenticationFieldState
import kotlin.coroutines.CoroutineContext

@Composable
fun UpdateUsername(
    usernameState: UsernameAuthenticationFieldState,
    currentUsername: String,
    modifier: Modifier = Modifier,
    label: String? = null,
    onClick: suspend (CoroutineContext) -> Unit
) {
    val usernameInput by usernameState.input.subscribeAsState()
    val usernameValid by usernameState.isValid.subscribeAsState()
    val scope = rememberCoroutineScope()

    SingleTextFieldRow(
        modifier = modifier,
        label = label ?: "Update username",
        input = usernameInput,
        isError = !usernameValid || label != null,
        enabled = currentUsername != usernameInput,
        leadingIcon = { Icon(imageVector = Outlined.AccountCircle, contentDescription = "Username") },
        onInputChange = usernameState::updateInput
    ) { scope.launch { onClick(this.coroutineContext) } }
}