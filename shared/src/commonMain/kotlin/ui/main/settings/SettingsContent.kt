package ui.main.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import component.main.settings.SettingsComponent
import util.ShapeTokens

@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(ShapeTokens.roundedCorners),
        color = colorScheme.tertiaryContainer.copy(alpha = .8f),
        contentColor = colorScheme.primary,
        tonalElevation = 24.dp,
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "SETTINGS VIEW",
                fontSize = typography.displayLarge.fontSize
            )
        }
    }
}