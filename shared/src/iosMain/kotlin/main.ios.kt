import androidx.compose.ui.window.ComposeUIViewController
import theme.ChatTheme
import theme.isSystemInDarkTheme

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController {
    ChatTheme(darkTheme = isSystemInDarkTheme()) { App() }
}