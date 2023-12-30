package util

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun textFieldColors(): TextFieldColors = colors(
    focusedContainerColor = Color.Transparent,
    focusedTextColor = colorScheme.primary,
    focusedLabelColor = colorScheme.primary,
    focusedLeadingIconColor = colorScheme.primary,
    focusedTrailingIconColor = colorScheme.primary,
    focusedBorderColor = colorScheme.primary,
    unfocusedContainerColor = Color.Transparent,
    unfocusedTextColor = colorScheme.secondary,
    unfocusedLabelColor = colorScheme.secondary,
    unfocusedLeadingIconColor = colorScheme.secondary,
    unfocusedBorderColor = colorScheme.secondary,
    unfocusedTrailingIconColor = colorScheme.secondary,
    errorTextColor = colorScheme.error,
    errorBorderColor = colorScheme.error,
    errorContainerColor = Color.Transparent,
    errorLeadingIconColor = colorScheme.error,
    errorTrailingIconColor = colorScheme.error,
    errorLabelColor = colorScheme.error,
    errorCursorColor = colorScheme.error,
    cursorColor = colorScheme.secondary
)

object ShapeTokens {
    val roundedCorners = 8.dp
}