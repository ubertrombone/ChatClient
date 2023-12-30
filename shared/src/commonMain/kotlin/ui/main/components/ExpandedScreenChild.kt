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
import ui.main.settings.SettingsContent
import util.Status

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
        if (logoutStatus is Status.Error) snackCallback((logoutStatus as Status.Error).message)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        NavRail(
            component = component,
            modifier = Modifier.fillMaxHeight().weight(.1f).background(colorScheme.primary).padding(12.dp)
        )

        ChildrenBox(childStack = childStack, modifier = Modifier.fillMaxHeight().weight(.9f))
    }

    settingsSlot.child?.instance?.also {
        SettingsContent(
            component = it,
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 60.dp)
        )
    }
}