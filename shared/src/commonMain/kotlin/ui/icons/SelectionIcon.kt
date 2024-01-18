package ui.icons

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun SelectionIcon(
    color: Color,
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    icon: @Composable () -> Unit
) {
    Surface(
        shape = shape,
        color = color,
        modifier = modifier,
        content = icon
    )
}