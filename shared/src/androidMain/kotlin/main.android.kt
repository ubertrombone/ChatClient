import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import component.root.RootComponent
import theme.ChatTheme

@Composable fun MainView(root: RootComponent) = ChatTheme(darkTheme = isSystemInDarkTheme()) { ChatApp(root) }
