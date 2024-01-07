package ui.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SingleTextFieldRow(
    label: String,
    input: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    enabled: Boolean = true,
    autoCorrect: Boolean = false,
    leadingIcon: @Composable () -> Unit = {},
    onInputChange: (String) -> Unit,
    onIconClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SettingsTextField(
            modifier = Modifier.weight(9f),
            input = input,
            onInputChange = onInputChange,
            label = label,
            leadingIcon = leadingIcon,
            isError = isError,
            autoCorrect = autoCorrect
        )

        IconButton(
            modifier = Modifier.size(40.dp),
            onClick = onIconClick,
            enabled = enabled,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = colorScheme.background,
                contentColor = if (isError) colorScheme.error else colorScheme.primary,
                disabledContentColor = colorScheme.onBackground
            )
        ) {
            Icon(imageVector = Filled.CheckCircle, contentDescription = "Submit")
        }
    }
}