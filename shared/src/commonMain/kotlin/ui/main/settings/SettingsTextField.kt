package ui.main.settings

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import util.textFieldColors

@Composable
fun SettingsTextField(
    label: String,
    input: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    autoCorrect: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    onInputChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = input,
        onValueChange = onInputChange,
        label = { Text(text = label, fontSize = MaterialTheme.typography.labelMedium.fontSize) },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isError = isError,
        singleLine = true,
        colors = textFieldColors(),
        keyboardOptions = KeyboardOptions(autoCorrect = autoCorrect, imeAction = imeAction, keyboardType = keyboardType),
        visualTransformation = visualTransformation
    )
}