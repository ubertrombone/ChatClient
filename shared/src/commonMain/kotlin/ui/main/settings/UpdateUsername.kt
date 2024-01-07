package ui.main.settings

import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import api.model.UpdateUsernameRequest
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.settings.SettingsComponent
import kotlinx.coroutines.launch
import ui.composables.states.UsernameAuthenticationFieldState
import util.toUsername

@Composable
fun UpdateUsername(
    component: SettingsComponent,
    usernameState: UsernameAuthenticationFieldState,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit
) {
    var label by remember { mutableStateOf<String?>(null) }
    val usernameInput by usernameState.input.subscribeAsState()
    val usernameValid by usernameState.isValid.subscribeAsState()
    val scope = rememberCoroutineScope()

    SingleTextFieldRow(
        modifier = modifier,
        label = label ?: "Update username",
        input = usernameInput,
        isError = !usernameValid || label != null,
        enabled = component.settings.username.get().lowercase() != usernameInput.lowercase(),
        leadingIcon = { Icon(imageVector = Outlined.AccountCircle, contentDescription = "Username") },
        onInputChange = usernameState::updateInput
    ) {
        scope.launch {
            component.updateUsernameStatus(usernameState.validateInput())
            if (usernameValid)
                component.updateUsername(UpdateUsernameRequest(usernameInput.toUsername()), coroutineContext)
            label = component.getUsernameAsResponse()
        }
        onSuccess()
    }
}