package util

import android.app.Activity
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import util.KeyboardMode.ADJUST_PAN
import util.KeyboardMode.ADJUST_RESIZE

@Composable
actual fun SoftInputMode(mode: Boolean) {
    with (LocalView.current) {
        if (!isInEditMode) {
            SideEffect {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
                    (context as Activity).window.setSoftInputMode(if (mode) ADJUST_RESIZE.mode else ADJUST_PAN.mode)
                else (context as Activity).window.setDecorFitsSystemWindows(mode)
            }
        }
    }
}