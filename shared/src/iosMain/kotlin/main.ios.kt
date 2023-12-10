import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import component.root.DefaultRootComponent
import platform.UIKit.UIViewController
import theme.ChatTheme
import theme.isSystemInDarkTheme

@Suppress("FunctionName", "unused")
fun MainViewController(): UIViewController {
    val lifecycle = LifecycleRegistry()
    val context = DefaultComponentContext(lifecycle = lifecycle)
    val root = DefaultRootComponent(componentContext = context)

    return ComposeUIViewController {
        ChatTheme(darkTheme = isSystemInDarkTheme()) {
            App(root)

            DisposableEffect(Unit) {
                lifecycle.resume()
                onDispose { lifecycle.destroy() }
            }
        }
    }
}