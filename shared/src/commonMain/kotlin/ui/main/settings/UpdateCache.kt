package ui.main.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import component.main.settings.SettingsComponent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
fun UpdateCache(
    initCache: Boolean,
    component: SettingsComponent,
    modifier: Modifier = Modifier,
    onSuccess: (Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    var cache by remember { mutableStateOf(initCache) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxHeight().weight(8f).padding(start = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Cache chats in server?",
                fontSize = typography.titleLarge.fontSize,
                color = colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp),
                maxLines = 1,
                textAlign = TextAlign.Start
            )

            Text(
                text = "Doing so allows you to sync your chats on different devices.",
                fontSize = typography.bodyMedium.fontSize,
                color = colorScheme.primary,
                modifier = Modifier.padding(end = 4.dp),
                maxLines = 1,
                textAlign = TextAlign.Start
            )
        }

        Switch(
            checked = cache,
            onCheckedChange = {
                scope.launch {
                    async { component.updateCache(it, this.coroutineContext) }.await()
                    onSuccess(it)
                }
                cache = it
            },
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}