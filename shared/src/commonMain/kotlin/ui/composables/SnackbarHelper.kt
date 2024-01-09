package ui.composables

import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHostState

suspend fun SnackbarHostState.snackbarHelper(
    message: String,
    actionLabel: String? = null,
    withDismissAction: Boolean = true
) {
    showSnackbar(
        message = message,
        actionLabel = actionLabel,
        withDismissAction = withDismissAction,
        duration = Short
    )
}