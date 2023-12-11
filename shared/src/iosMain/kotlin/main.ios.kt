import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.russhwolf.settings.NSUserDefaultsSettings
import component.root.DefaultRootComponent
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIViewController
import settings.SettingsRepository
import theme.ChatTheme
import theme.isSystemInDarkTheme

@Suppress("FunctionName", "unused")
fun MainViewController(defaults: NSUserDefaults): UIViewController {
    val lifecycle = LifecycleRegistry()
    val context = DefaultComponentContext(lifecycle = lifecycle)
    val settings = NSUserDefaultsSettings(defaults)
    val settingsRepository = SettingsRepository(settings)
    val root = DefaultRootComponent(componentContext = context, settingsRepository = settingsRepository)

    return ComposeUIViewController {
        ChatTheme(darkTheme = isSystemInDarkTheme()) {
            ChatApp(root)

            DisposableEffect(Unit) {
                lifecycle.resume()
                onDispose { lifecycle.destroy() }
            }
        }
    }
}