package ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import util.ShapeTokens
import util.textFieldColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PasswordField(
    password: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
) {
    var visibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onValueChange,
        label = { Text(text = "Password", fontSize = typography.labelMedium.fontSize) },
        leadingIcon = { Icon(imageVector = Outlined.Lock, contentDescription = "Username") },
        trailingIcon = {
            Icon(
                painter = painterResource(if (visibility) "visibility_off.xml" else "visibility.xml"),
                contentDescription = "Show Password?",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { visibility = !visibility }
            )
        },
        visualTransformation = if (visibility) VisualTransformation.None else PasswordVisualTransformation(),
        colors = textFieldColors(),
        shape = RoundedCornerShape(ShapeTokens.roundedCorners),
        singleLine = true,
        isError = isError,
        modifier = modifier
    )
}