package ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.MainComponent
import component.main.MainComponent.Child.*
import util.Status.Error

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(component: MainComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val activeComponent = childStack.active.instance
    val logoutStatus by component.logoutStatus.subscribeAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // TODO:
    //  Components:
    //      1. Bottom bar - Friends; group chats; settings
    //      2. Add friend button - top bar action item (icon button) - WHAT IF SETTINGS IS IN THE TOP BAR AND ADD FRIEND/FRIEND REQUESTS IS IN THE BOTTOM BAR?
    //          - popup with search bar to search for friends
    //          - Requires slot stack component
    //      3. friend list in lazy column
    //          - immutable list returned by API
    //      4. Friend Requests
    //          - similar to add friend button
    //          - requires own navigation component

    LaunchedEffect(logoutStatus) {
        if (logoutStatus is Error) snackbarHostState.showSnackbar(
            message = (logoutStatus as Error).message,
            actionLabel = "Dismiss",
            duration = SnackbarDuration.Short
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = component.title,
                        fontSize = typography.displayLarge.fontSize,
                        fontWeight = typography.displayLarge.fontWeight,
                    )
                },
                actions = {// TODO: Temporary
                    TextButton(
                        modifier = Modifier.padding(end = 24.dp),
                        onClick = component::logout
                    ) {
                        Text(
                            text = "Logout",
                            fontSize = typography.bodyMedium.fontSize,
                            fontWeight = typography.bodyMedium.fontWeight
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = colorScheme.primary) {
                NavigationBarItem(
                    label = { Text(text = "Chat") },
                    icon = { Icon(imageVector = Icons.Default.List, contentDescription = "Friends list") },
                    selected = activeComponent is ChatChild,
                    onClick = component::onChatsTabClicked,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = colorScheme.onPrimary,
                        unselectedTextColor = colorScheme.onPrimary,
                        selectedIconColor = colorScheme.onPrimaryContainer,
                        selectedTextColor = colorScheme.primaryContainer,
                        indicatorColor = colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    label = { Text(text = "Group Chats", softWrap = true) },
                    icon = { Icon(imageVector = Icons.Default.Menu, contentDescription = "Group chats") }, // TODO: Icon
                    selected = activeComponent is GroupChild,
                    onClick = component::onGroupChatsTabClicked,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = colorScheme.onPrimary,
                        unselectedTextColor = colorScheme.onPrimary,
                        selectedIconColor = colorScheme.onPrimaryContainer,
                        selectedTextColor = colorScheme.primaryContainer,
                        indicatorColor = colorScheme.primaryContainer
                    )
                )

                NavigationBarItem(
                    label = { Text(text = "Add Friend", softWrap = true) },
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Add friend") },
                    selected = activeComponent is AddChild,
                    onClick = component::onAddTabClicked,
                    alwaysShowLabel = false,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = colorScheme.onPrimary,
                        unselectedTextColor = colorScheme.onPrimary,
                        selectedIconColor = colorScheme.onPrimaryContainer,
                        selectedTextColor = colorScheme.primaryContainer,
                        indicatorColor = colorScheme.primaryContainer
                    )
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorScheme.background
    ) {
        // TODO: Children here
        Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
            Text(
                text = "MAIN VIEW",
                fontSize = typography.displayLarge.fontSize
            )
        }
    }
}