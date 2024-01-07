package ui.main.settings

import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import component.main.settings.SettingsComponent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.states.StatusAuthenticationFieldState
import util.Constants.MAX_STATUS_LENGTH
import util.Constants.STATUS_TOO_LONG

@OptIn(ExperimentalResourceApi::class)
@Composable
fun UpdateStatus(
    component: SettingsComponent,
    statusState: StatusAuthenticationFieldState,
    currentStatus: String?,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit
) {
    var label by remember { mutableStateOf<String?>(null) }
    val statusInput by statusState.input.collectAsState()
    val isValid by derivedStateOf { statusInput.length <= MAX_STATUS_LENGTH }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isValid) {
        if (!isValid) label = "$STATUS_TOO_LONG Current: ${statusInput.length}"
    }

    SingleTextFieldRow(
        modifier = modifier,
        label = label ?: "Update status",
        input = statusInput,
        isError = !isValid || label != null,
        autoCorrect = true,
        enabled = currentStatus != statusInput,
        leadingIcon = { Icon(painter = painterResource("sentiment_satisfied.xml"), contentDescription = "Status") },
        onInputChange = statusState::updateInput
    ) {
        scope.launch { if (isValid) component.updateStatus(statusInput, coroutineContext) }
        onSuccess()
    }
}