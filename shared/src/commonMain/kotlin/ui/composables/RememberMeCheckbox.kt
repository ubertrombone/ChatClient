package ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RememberMeCheckbox(
    rememberMe: Boolean,
    modifier: Modifier = Modifier,
    onCheckChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Checkbox(
            checked = rememberMe,
            onCheckedChange = onCheckChanged,
            colors = CheckboxDefaults.colors(
                checkedColor = colorScheme.primary,
                checkmarkColor = colorScheme.onPrimary,
                uncheckedColor = colorScheme.onSurfaceVariant
            )
        )

        Text(
            text = "Remember me",
            color = colorScheme.primary,
            fontSize = typography.labelLarge.fontSize
        )
    }
}