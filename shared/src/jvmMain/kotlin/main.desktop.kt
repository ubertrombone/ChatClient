import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jthemedetecor.OsThemeDetector
import component.root.RootComponent
import org.jetbrains.skiko.SystemTheme
import org.jetbrains.skiko.currentSystemTheme
import theme.ChatTheme

@Composable
fun MainView(
    root: RootComponent,
    isMaximized: Boolean,
    minimizeWindow: () -> Unit,
    adjustWindow: () -> Unit,
    closeRequest: () -> Unit,
    saveContent: @Composable () -> Unit
) = ChatTheme(darkTheme = rememberDesktopDarkTheme()) {
    App(root)
    saveContent()
}

@Composable
fun rememberDesktopDarkTheme(): Boolean {
    var darkTheme by remember {
        mutableStateOf(currentSystemTheme == SystemTheme.DARK)
    }

    DisposableEffect(Unit) {
        val darkThemeListener: (Boolean) -> Unit = {
            darkTheme = it
        }

        val detector = OsThemeDetector.getDetector().apply {
            registerListener(darkThemeListener)
        }

        onDispose {
            detector.removeListener(darkThemeListener)
        }
    }

    return darkTheme
}