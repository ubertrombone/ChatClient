package ui.main.add

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ui.composables.states.StatusAuthenticationFieldState
import util.Constants
import util.textFieldColors

@Composable
fun QueryField(
    state: StatusAuthenticationFieldState,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val queryInput by state.input.collectAsState()
    val input by remember(queryInput) { mutableStateOf(queryInput) }

    OutlinedTextField(
        value = input,
        onValueChange = {
            if (it.length <= Constants.REQUIREMENT_MAX) {
                onValueChange(it)
                state.updateInput(it)
            }
        },
        modifier = modifier,
        placeholder = { Text(text = "Search for Friends") },
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Done),
        colors = textFieldColors(),
        shape = RoundedCornerShape(36.dp)
    )
}