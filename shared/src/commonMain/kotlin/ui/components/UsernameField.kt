package ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import util.ShapeTokens
import util.textFieldColors

@Composable
fun UsernameField(
    usernameInput: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = usernameInput,
        onValueChange = onValueChange,
        label = { Text(text = "Username", fontSize = typography.labelMedium.fontSize) },
        leadingIcon = { Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = "Username") },
        colors = textFieldColors(),
        shape = RoundedCornerShape(ShapeTokens.roundedCorners),
        singleLine = true,
        isError = isError,
        modifier = modifier
    )
}