package ui.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import ui.composables.states.UsernameAuthenticationFieldState
import util.textFieldColors
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

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            modifier = Modifier.weight(9f),
            value = usernameInput,
            onValueChange = usernameState::updateInput,
            label = { Text(text = label ?: "", fontSize = typography.labelMedium.fontSize) },
            placeholder = { Text(text = "Update Username") },
            leadingIcon = { Icon(imageVector = Outlined.AccountCircle, contentDescription = "Username") },
            isError = !usernameValid || label != null,
            singleLine = true,
            colors = textFieldColors()
        )

        IconButton(
            modifier = Modifier.size(40.dp),
            onClick = { scope.launch { onClick(this.coroutineContext) } },
            enabled = currentUsername != usernameInput,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = colorScheme.background,
                contentColor = if (!usernameValid || label != null) colorScheme.error else colorScheme.primary,
                disabledContentColor = colorScheme.onBackground
            )
        ) {
            Icon(imageVector = Filled.CheckCircle, contentDescription = "Submit")
        }
    }
}