package ui.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.MainComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import util.Constants.CHATS
import util.Constants.GROUP_CHATS
import util.Constants.REQUESTS

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NavRail(
    component: MainComponent,
    modifier: Modifier = Modifier
) {
    val settingsSlot by component.settingsSlot.subscribeAsState()
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance

    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = modifier) {
        NavigationRail(modifier = Modifier.align(Alignment.TopCenter), containerColor = colorScheme.primary) {
            NavRailItem(
                label = CHATS,
                icon = { Icon(painter = painterResource("chat.xml"), contentDescription = "Friends list") },
                selected = activeComponent is MainComponent.Child.ChatChild,
                onClick = component::onChatsTabClicked,
            )

            NavRailItem(
                label = GROUP_CHATS,
                icon = { Icon(painter = painterResource("groups.xml"), contentDescription = "Group chats") },
                selected = activeComponent is MainComponent.Child.GroupChild,
                onClick = component::onGroupChatsTabClicked
            )

            NavRailItem(
                label = REQUESTS,
                icon = { Icon(painter = painterResource("add_friend.xml"), contentDescription = "Friend requests") },
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

        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "Logout",
            tint = colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .clip(CircleShape)
                .clickable(
                    indication = rememberRipple(color = colorScheme.primaryContainer),
                    interactionSource = interactionSource,
                    onClick = component::logout
                )
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}