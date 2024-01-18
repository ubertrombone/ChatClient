package ui.main.add.requests.received

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.received.ReceivedRequestsComponent
import io.ktor.client.statement.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.composables.snackbarHelper
import ui.icons.SearchIcon
import ui.icons.SentRequestsIcon
import util.Status.Error

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun CompactReceivedContent(component: ReceivedRequestsComponent, modifier: Modifier = Modifier) {
    val snackbarHostState = component.snackbarHostState
    val actionStatus by component.actionStatus.subscribeAsState()

    LaunchedEffect(actionStatus) {
        if (actionStatus is Error) snackbarHostState.snackbarHelper(
            message = ((actionStatus as Error).body as HttpResponse).bodyAsText()
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(bottom = 12.dp),
                title = {},
                navigationIcon = { SearchIcon { component.dismiss() } },
                actions = {
                    SentRequestsIcon(
                        modifier = Modifier
                            .padding(top = 12.dp, end = 12.dp)
                            .padding(5.dp)
                    ) { component.navToSent() }
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
    ) {
        ReceivedContent(component = component, snackbarHostState = snackbarHostState, modifier = Modifier.fillMaxSize().padding(it))
    }
}