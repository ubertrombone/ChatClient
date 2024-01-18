package ui.main.add.requests.received

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.received.ReceivedRequestsComponent
import io.ktor.client.statement.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.snackbarHelper
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
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Return to search users",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 12.dp)
                            .size(40.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .clickable { component.dismiss() }
                    )
                },
                actions = {
                    // TODO: Extract
                    Icon(
                        painter = painterResource("outgoing.xml"),
                        contentDescription = "Sent Requests",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .padding(top = 12.dp, end = 12.dp)
                            .size(40.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .clickable { component.navToSent() }
                    )
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