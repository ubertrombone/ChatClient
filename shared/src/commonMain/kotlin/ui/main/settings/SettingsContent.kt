package ui.main.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import component.main.settings.SettingsComponent

@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "SETTINGS VIEW",
            fontSize = typography.displayLarge.fontSize
        )
    }
}