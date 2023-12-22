package ui.composables

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import ui.composables.states.AuthenticationFieldState
import util.ShapeTokens
import util.textFieldColors

@Composable
fun UsernameField(
    state: AuthenticationFieldState,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val input by state.input.subscribeAsState()
    val isValid by state.isValid.subscribeAsState()
    
    OutlinedTextField(
        value = input,
        onValueChange = state::updateInput,
        label = { Text(text = "Username", fontSize = typography.labelMedium.fontSize) },
        leadingIcon = { Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Username") },
        colors = textFieldColors(),
        shape = RoundedCornerShape(ShapeTokens.roundedCorners),
        singleLine = true,
        isError = !isValid,
        enabled = enabled,
        modifier = modifier
    )
}