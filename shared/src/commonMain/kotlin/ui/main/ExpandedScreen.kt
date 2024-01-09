package ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import component.main.MainComponent
import kotlinx.coroutines.launch
import ui.composables.snackbarHelper
import ui.main.components.ExpandedScreenChild
import util.BottomBarSystemNavColor

@Composable
fun ExpandedScreen(component: MainComponent, modifier: Modifier = Modifier) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BottomBarSystemNavColor(colorScheme.primary)

    Scaffold(
        modifier = modifier,
//        topBar = {
//            TopAppBar( // TODO: Make this an expect/actual fun? JVM doesn't need a title on every screen
//                title = {
//                    Text(
//                        text = component.title,
//                        fontSize = MaterialTheme.typography.displayLarge.fontSize,
//                        fontWeight = MaterialTheme.typography.displayLarge.fontWeight,
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = colorScheme.background,
//                    titleContentColor = colorScheme.primary
//                )
//            )
//        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = colorScheme.background
    ) {
        ExpandedScreenChild(component, Modifier.fillMaxSize().padding(it)) { message ->
            scope.launch {
                snackbarHostState.snackbarHelper(message = message, actionLabel = "Dismiss")
            }
        }
    }
}