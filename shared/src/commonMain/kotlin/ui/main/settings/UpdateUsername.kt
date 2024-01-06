package ui.main.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch
import ui.composables.states.UsernameAuthenticationFieldState
import util.textFieldColors
import kotlin.coroutines.CoroutineContext

@Composable
fun UpdateUsername(
    usernameState: UsernameAuthenticationFieldState,
    modifier: Modifier = Modifier,
    label: String? = null,
    onClick: suspend (CoroutineContext) -> Unit
) {
    val usernameInput by usernameState.input.subscribeAsState()
    val usernameValid by usernameState.isValid.subscribeAsState()
    val scope = rememberCoroutineScope()

    TextField(
        modifier = modifier,
        value = usernameInput,
        onValueChange = usernameState::updateInput,
        label = { Text(text = label ?: "", fontSize = typography.labelMedium.fontSize) },
        placeholder = { Text(text = "Update Username") },
        leadingIcon = { Icon(imageVector = Outlined.AccountCircle, contentDescription = "Username") },
        trailingIcon = {
            Icon(
                imageVector = Filled.CheckCircle,
                contentDescription = "Submit",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { scope.launch { onClick(this.coroutineContext) } }
            )
        },
        isError = !usernameValid || label != null,
        singleLine = true,
        colors = textFieldColors()
    )
}