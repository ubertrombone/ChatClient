package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ExportObjCClass
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.UIKit.*

@Composable
actual fun ChatTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun isSystemInDarkTheme(): Boolean {
    var style: UIUserInterfaceStyle by remember {
        mutableStateOf(UITraitCollection.currentTraitCollection.userInterfaceStyle)
    }

    val viewController: UIViewController = LocalUIViewController.current
    DisposableEffect(Unit) {
        val view: UIView = viewController.view
        val traitView = TraitView {
            style = UITraitCollection.currentTraitCollection.userInterfaceStyle
        }
        view.addSubview(traitView)

        onDispose {
            traitView.removeFromSuperview()
        }
    }

    return style == UIUserInterfaceStyle.UIUserInterfaceStyleDark
}

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
@ExportObjCClass
private class TraitView(
    private val onTraitChanged: () -> Unit
) : UIView(frame = CGRectZero.readValue()) {
    override fun traitCollectionDidChange(previousTraitCollection: UITraitCollection?) {
        super.traitCollectionDidChange(previousTraitCollection)
        onTraitChanged()
    }
}