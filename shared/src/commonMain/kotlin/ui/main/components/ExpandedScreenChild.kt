package ui.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.MainComponent
import component.main.MainComponent.Child.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.main.settings.SettingsContent
import util.Status

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpandedScreenChild(
    component: MainComponent,
    modifier: Modifier = Modifier,
    snackCallback: (String) -> Unit
) {
    val settingsSlot by component.settingsSlot.subscribeAsState()
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance
    val logoutStatus by component.logoutStatus.subscribeAsState()

    LaunchedEffect(logoutStatus) {
        if (logoutStatus is Status.Error) snackCallback((logoutStatus as Status.Error).message)
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start
    ) {
        NavigationRail(
            modifier = Modifier.weight(.1f),
            containerColor = colorScheme.primary
        ) {
            NavRailItem(
                label = "Chat",
                icon = { Icon(painter = painterResource("chat.xml"), contentDescription = "Friends list") },
                selected = activeComponent is ChatChild,
                onClick = component::onChatsTabClicked,
            )
        }
    }

    settingsSlot.child?.instance?.also {
        SettingsContent(
            component = it,
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 60.dp)
        )
    }
}