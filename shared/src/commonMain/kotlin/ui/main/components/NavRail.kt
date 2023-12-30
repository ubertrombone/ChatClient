package ui.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.MainComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NavRail(
    component: MainComponent,
    modifier: Modifier = Modifier
) {
    val settingsSlot by component.settingsSlot.subscribeAsState()
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NavigationRail(containerColor = colorScheme.primary) {
            NavRailItem(
                label = "Chat",
                icon = { Icon(painter = painterResource("chat.xml"), contentDescription = "Friends list") },
                selected = activeComponent is MainComponent.Child.ChatChild,
                onClick = component::onChatsTabClicked,
            )

            NavRailItem(
                label = "Group Chats",
                icon = { Icon(painter = painterResource("groups.xml"), contentDescription = "Group chats") },
                selected = activeComponent is MainComponent.Child.GroupChild,
                onClick = component::onGroupChatsTabClicked
            )

            NavRailItem(
                label = "Add Friend",
                icon = { Icon(painter = painterResource("add_friend.xml"), contentDescription = "Add friend") },
                selected = activeComponent is MainComponent.Child.AddChild,
                onClick = component::onAddTabClicked
            )

            NavRailItem(
                label = "Settings",
                icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings") },
                selected = settingsSlot.child != null,
                onClick = if (settingsSlot.child == null) component::showSettings else component::dismissSettings
            )
        }

        IconButton(
            modifier = Modifier.padding(end = 12.dp),
            onClick = component::logout
        ) {
            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
        }
    }
}