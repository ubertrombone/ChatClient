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
import util.Status
import util.Status.Success
import util.toUsername

@Composable
fun UpdateUsername(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
    onSuccess: (Status) -> Unit
) {
    val usernameStatus by component.usernameStatus.subscribeAsState()
    var label by remember { mutableStateOf<String?>(null) }
    var currentUsername by remember { mutableStateOf(component.settings.username.get()) }
    val usernameInput by component.usernameInput.subscribeAsState()
    val input by remember(usernameInput) { mutableStateOf(usernameInput) }
    val usernameValid by component.usernameIsValid.subscribeAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(usernameStatus) { label = component.getUsernameAsResponse() }

    SingleTextFieldRow(
        modifier = modifier,
        label = label ?: "Update username",
        input = input,
        isError = !usernameValid || label != null,
        enabled = currentUsername.lowercase() != input.lowercase(),
        leadingIcon = { Icon(imageVector = Outlined.AccountCircle, contentDescription = "Username") },
        onInputChange = component::updateUsernameInput
    ) {
        scope.launch {
            // This function checks that the input entered is a valid username
            component.updateUsernameStatus(component.validateUsernameInput())
            // If the input is a valid username, call the API
            if (usernameValid)
                component.updateUsername(UpdateUsernameRequest(input.toUsername()), coroutineContext)
            // Only if the api call is successful use the onSuccess callback to show a success snackbar message
            if (usernameStatus == Success) {
                currentUsername = input
                onSuccess(usernameStatus)
            }
        }
    }
}