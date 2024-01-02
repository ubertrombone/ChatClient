package ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.MainComponent
import io.ktor.client.statement.*
import ui.main.settings.SettingsContent
import util.Status.Error

@Composable
fun ExpandedScreenChild(
    component: MainComponent,
    modifier: Modifier = Modifier,
    snackCallback: (String) -> Unit
) {
    val settingsSlot by component.settingsSlot.subscribeAsState()
    val childStack by component.childStack.subscribeAsState()
    val logoutStatus by component.logoutStatus.subscribeAsState()

    LaunchedEffect(logoutStatus) {
        runCatching { logoutStatus as Error<*> }.getOrNull()?.let {
            snackCallback((it.body as HttpResponse).status.description)
        }
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        NavRail(
            component = component,
            modifier = Modifier.fillMaxHeight().width(100.dp).background(colorScheme.primary).padding(12.dp)
        )

        settingsSlot.child?.instance?.also {
            SettingsContent(
                component = it,
                modifier = Modifier.fillMaxHeight().weight(.9f)
            )
        } ?: ChildrenBox(childStack = childStack, modifier = Modifier.fillMaxHeight().weight(.9f))
    }
}