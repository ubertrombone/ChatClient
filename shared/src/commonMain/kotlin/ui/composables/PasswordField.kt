package ui.composables

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.states.AuthenticationFieldState
import util.ShapeTokens
import util.textFieldColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PasswordField(
    state: AuthenticationFieldState,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    label: String = "Password"
) {
    val input by state.input.subscribeAsState()
    val isValid by state.isValid.subscribeAsState()
    var visibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = input,
        onValueChange = state::updateInput,
        label = { Text(text = label, fontSize = typography.labelMedium.fontSize) },
        leadingIcon = { Icon(imageVector = Outlined.Lock, contentDescription = "Username") },
        trailingIcon = {
            IconToggleButton(
                checked = visibility,
                onCheckedChange = { visibility = !visibility },
                modifier = Modifier.clip(CircleShape)
            ) {
                Icon(
                    painter = painterResource(if (visibility) "visibility_off.xml" else "visibility.xml"),
                    contentDescription = "Show Password?"
                )
            }
        },
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
        colors = textFieldColors(),
        shape = RoundedCornerShape(ShapeTokens.roundedCorners),
        singleLine = true,
        isError = !isValid,
        enabled = enabled,
        modifier = modifier
    )
}