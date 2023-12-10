import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.statekeeper.SerializableContainer
import com.arkivanov.essenty.statekeeper.StateKeeperDispatcher
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import java.awt.Dimension
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

@OptIn(ExperimentalDecomposeApi::class)
fun main() {
    val lifecycle = LifecycleRegistry()
    val stateKeeper = StateKeeperDispatcher(tryRestoreStateFromFile())

    Napier.base(DebugAntilog())

    application {
        val windowParams = DpSize(750.dp, 750.dp)
        val windowState = rememberWindowState(size = windowParams, placement = WindowPlacement.Floating)

        LifecycleController(lifecycle, windowState)

        var isCloseRequested by remember { mutableStateOf(false) }

        Window(
            onCloseRequest = { isCloseRequested = true },
            state = windowState,
            resizable = true,
            title = "Chat Client",
            transparent = true,
            undecorated = true
        ) {
            window.minimumSize = Dimension(
                windowParams.width.value.toInt(),
                windowParams.height.value.toInt()
            )

            WindowDraggableArea {
                MainView()
            }
        }
    }
}

@Composable
private fun SaveStateDialog(
    onSaveState: () -> Unit,
    onExitApplication: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(
            text = "Plots for Compose Sample",
            style = typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
        ) },
        text = { Text(text = "Do you want to save the application's state?", color = colorScheme.primary) },
        buttons ={
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(text = "Cancel", color = colorScheme.primary)
                }

                TextButton(onClick = onExitApplication) {
                    Text(text = "No", color = colorScheme.primary)
                }

                TextButton(
                    onClick = {
                        onSaveState()
                        onExitApplication()
                    }
                ) {
                    Text(text = "Yes", color = colorScheme.primary)
                }
            }
        },
        backgroundColor = colorScheme.primaryContainer,
        contentColor = colorScheme.primary,
        shape = RoundedCornerShape(12.dp)
    )
}

private const val SAVED_STATE_FILE_NAME = "saved_state.dat"

private fun saveStateToFile(state: SerializableContainer) {
    ObjectOutputStream(File(SAVED_STATE_FILE_NAME).outputStream()).use { output ->
        output.writeObject(state)
    }
}

private fun tryRestoreStateFromFile(): SerializableContainer? =
    File(SAVED_STATE_FILE_NAME).takeIf(File::exists)?.let { file ->
        try {
            ObjectInputStream(file.inputStream()).use(ObjectInputStream::readObject) as SerializableContainer
        } catch (e: Exception) {
            null
        } finally {
            file.delete()
        }
    }