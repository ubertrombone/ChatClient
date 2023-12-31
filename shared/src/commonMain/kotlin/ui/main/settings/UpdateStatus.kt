package ui.main.settings

import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.settings.SettingsComponent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.states.StatusAuthenticationFieldState
import util.Constants.MAX_STATUS_LENGTH
import util.Constants.STATUS_TOO_LONG
import util.Status
import util.Status.Success

@OptIn(ExperimentalResourceApi::class)
@Composable
fun UpdateStatus(
    component: SettingsComponent,
    statusState: StatusAuthenticationFieldState,
    modifier: Modifier = Modifier,
    onSuccess: (Status) -> Unit
) {
    val statusStatus by component.updateStatusStatus.subscribeAsState()
    var label by remember { mutableStateOf<String?>(null) }
    var currentStatus by remember { mutableStateOf(component.settings.status.get()) }
    val statusInput by statusState.input.collectAsState()
    val isValid by derivedStateOf { statusInput.length <= MAX_STATUS_LENGTH }
    val scope = rememberCoroutineScope()

    LaunchedEffect(isValid) {
        label = if (!isValid) "$STATUS_TOO_LONG Current: ${statusInput.length}" else null
    }

    SingleTextFieldRow(
        modifier = modifier,
        label = label ?: "Update status",
        input = statusInput,
        isError = !isValid || label != null,
        autoCorrect = true,
        enabled = currentStatus != statusInput && label == null,
        leadingIcon = { Icon(painter = painterResource("sentiment_satisfied.xml"), contentDescription = "Status") },
        onInputChange = statusState::updateInput
    ) {
        scope.launch {
            if (isValid) component.updateStatus(statusInput, coroutineContext)
            if (statusStatus == Success) {
                currentStatus = statusInput
                onSuccess(statusStatus)
            }
        }
    }
}