package ui.main.add.requests

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.RequestComponent
import component.main.add.requests.RequestComponent.Child.ReceivedChild
import component.main.add.requests.RequestComponent.Child.SentChild
import ui.icons.SearchIcon
import ui.icons.SentRequestsIcon
import ui.main.add.requests.received.ReceivedContent
import ui.main.add.requests.sent.SentContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactRequestsContent(component: RequestComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()
    val snackbarHostState = component.snackbarHostState

    Children(stack = childStack, modifier = modifier) {
        Scaffold(
            modifier = modifier,
            topBar = {
                CenterAlignedTopAppBar(
                    modifier = Modifier.padding(bottom = 12.dp),
                    title = {},
                    navigationIcon = {
                        SearchIcon {
                            when (val child = it.instance) {
                                is ReceivedChild -> child.component.dismiss()
                                is SentChild -> child.component.dismiss()
                            }
                        }
                    },
                    actions = {
                        SentRequestsIcon(
                            modifier = Modifier
                                .padding(top = 12.dp, end = 12.dp)
                                .padding(5.dp)
                        ) {
                            when (val child = it.instance) {
                                is ReceivedChild -> child.component.navToSent()
                                is SentChild -> child.component.navBack()
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = colorScheme.background,
                        titleContentColor = colorScheme.primary
                    )
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    Snackbar(
                        snackbarData = data,
                        containerColor = colorScheme.primaryContainer.copy(alpha = .7f), // TODO: Something wrong here in light mode
                        contentColor = colorScheme.onPrimaryContainer,
                        dismissActionContentColor = colorScheme.onPrimaryContainer
                    )
                }
            },
            containerColor = colorScheme.background
        ) { padding ->
            when (val child = it.instance) {
                is ReceivedChild -> ReceivedContent(
                    component = child.component,
                    snackbarHostState = snackbarHostState,
                    modifier = Modifier.fillMaxSize().padding(padding)
                )
                is SentChild -> SentContent(component = child.component, modifier = Modifier.fillMaxSize().padding(padding))
            }
        }
    }
}