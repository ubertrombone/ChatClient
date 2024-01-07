package ui.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import util.textFieldColors

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
        TextField(
            modifier = Modifier.weight(9f),
            value = input,
            onValueChange = onInputChange,
            label = { Text(text = label, fontSize = typography.labelMedium.fontSize) },
            leadingIcon = leadingIcon,
            isError = isError,
            singleLine = true,
            colors = textFieldColors(),
            keyboardOptions = KeyboardOptions(autoCorrect = autoCorrect, imeAction = ImeAction.Done)
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