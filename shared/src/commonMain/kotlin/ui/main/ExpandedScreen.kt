package ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import component.main.MainComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import ui.main.components.ExpandedScreenChild
import util.BottomBarSystemNavColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedScreen(component: MainComponent, modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }

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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorScheme.background
    ) {
        ExpandedScreenChild(component, Modifier.fillMaxSize().padding(it)) { message ->
            runBlocking(Dispatchers.Main) { // TODO: I don't like this
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "Dismiss",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}