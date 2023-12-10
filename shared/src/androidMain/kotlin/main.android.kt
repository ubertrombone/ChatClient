import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import theme.ChatTheme

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = ChatTheme(darkTheme = isSystemInDarkTheme()) { App() }
