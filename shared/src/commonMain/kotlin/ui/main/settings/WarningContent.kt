package ui.main.settings

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import component.main.settings.warning.DeleteAccountComponent
import kotlinx.coroutines.launch
import ui.composables.DialogButton
import util.ShapeTokens

@Composable
fun WarningContent(component: DeleteAccountComponent, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()

    AlertDialog(
        modifier = modifier,
        onDismissRequest = { component.dismiss() },
        confirmButton = {
            DialogButton("Confirm") { scope.launch { component.deleteAccount(this.coroutineContext) } }
        },
        dismissButton = { DialogButton("Cancel") { component.dismiss() } },
        icon = { Icon(imageVector = Icons.Default.Warning, contentDescription = null) },
        title = {
            Text(
                text = "Delete Account?",
                style = typography.headlineMedium,
                fontWeight = Bold
            )
        },
        text = { Text(text = "Are you sure you want to delete your account? This action cannot be undone.") },
        shape = RoundedCornerShape(ShapeTokens.roundedCorners),
        containerColor = colorScheme.primaryContainer,
        iconContentColor = Color.Yellow,
        titleContentColor = colorScheme.primary,
        textContentColor = colorScheme.primary,
    )
}