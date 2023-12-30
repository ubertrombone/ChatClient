package ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.MainComponent
import util.BottomBarSystemNavColor
import util.Status

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedScreen(component: MainComponent, modifier: Modifier = Modifier) {
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
    BottomBarSystemNavColor(MaterialTheme.colorScheme.primary)

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) {

    }
}