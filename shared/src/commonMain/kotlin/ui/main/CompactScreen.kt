package ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.MainComponent
import component.main.MainComponent.Child.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.main.components.NavBarItem
import ui.main.components.ChildrenBox
import ui.main.settings.SettingsContent
import util.BottomBarSystemNavColor
import util.Constants
import util.Constants.CHATS
import util.Constants.GROUP_CHATS
import util.Constants.REQUESTS
import util.Status

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun CompactScreen(component: MainComponent, modifier: Modifier = Modifier) {
    val settingsSlot by component.settingsSlot.subscribeAsState()
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance
    val logoutStatus by component.logoutStatus.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(logoutStatus) {
        if (logoutStatus is Status.Error) snackbarHostState.showSnackbar(
            message = (logoutStatus as Status.Error).message,
            actionLabel = "Dismiss",
            duration = SnackbarDuration.Short
        )
    }
    BottomBarSystemNavColor(colorScheme.primary)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar( // TODO: Make this an expect/actual fun? JVM doesn't need a title on every screen
                title = {
                    Text(
                        text = component.title,
                        fontSize = MaterialTheme.typography.displayLarge.fontSize,
                        fontWeight = MaterialTheme.typography.displayLarge.fontWeight,
                    )
                },
                actions = {
                    IconButton(
                        modifier = Modifier.padding(end = 12.dp),
                        onClick = component::logout
                    ) {
                        Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout")
                    }

                    IconButton(
                        modifier = Modifier.padding(24.dp),
                        onClick = if (settingsSlot.child == null) component::showSettings else component::dismissSettings
                    ) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary,
                    actionIconContentColor = colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = colorScheme.primary) {
                NavBarItem(
                    label = CHATS,
                    icon = { Icon(painter = painterResource("chat.xml"), contentDescription = "Friends list") },
                    selected = activeComponent is ChatChild,
                    onClick = component::onChatsTabClicked
                )

                NavBarItem(
                    label = GROUP_CHATS,
                    icon = { Icon(painter = painterResource("groups.xml"), contentDescription = "Group chats") },
                    selected = activeComponent is GroupChild,
                    onClick = component::onGroupChatsTabClicked
                )

                NavBarItem(
                    label = REQUESTS,
                    icon = { Icon(painter = painterResource("add_friend.xml"), contentDescription = "Friend requests") },
                    selected = activeComponent is AddChild,
                    onClick = component::onAddTabClicked
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorScheme.background
    ) {
        ChildrenBox(childStack = childStack, modifier = Modifier.fillMaxSize().padding(it))
    }

    settingsSlot.child?.instance?.also {
        SettingsContent(
            component = it,
            modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp, vertical = 60.dp)
        )
    }
}