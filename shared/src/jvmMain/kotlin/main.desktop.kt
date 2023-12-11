import androidx.compose.runtime.*
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
    ChatApp(root)
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