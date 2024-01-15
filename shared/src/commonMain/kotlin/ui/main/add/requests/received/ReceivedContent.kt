package ui.main.add.requests.received

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.received.ReceivedRequestsComponent
import io.ktor.client.statement.*
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.expect.ScrollLazyColumn
import ui.composables.snackbarHelper
import ui.main.add.AddCard
import util.Status.Error
import util.Status.Success

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ReceivedContent(component: ReceivedRequestsComponent, modifier: Modifier = Modifier) {
    val snackbarHostState = component.snackbarHostState
    val requests by component.receivedList.subscribeAsState()
    val receivedStatus by component.listStatus.subscribeAsState()
    val receivedLoading by component.listLoading.subscribeAsState()
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
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                receivedLoading -> CircularProgressIndicator(
                    modifier = Modifier.size(120.dp).align(Alignment.Center),
                    color = colorScheme.primary,
                    trackColor = colorScheme.surfaceVariant
                )

                receivedStatus is Error -> Text(
                    text = (receivedStatus as Error).body.toString(),
                    modifier = Modifier.align(Alignment.Center)
                )

                receivedStatus == Success -> {
                    if (requests.reqs.isEmpty()) Text(
                        text = "You haven't received any requests.",
                        modifier = Modifier.align(Alignment.Center)
                    ) else ScrollLazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(requests.reqs.toImmutableList()) {
                            AddCard(
                                label = "${it.requesterUsername.name} would like to add you!",
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    modifier = Modifier.padding(end = 12.dp),
                                    onClick = { component.acceptRequest(it) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Done,
                                        contentDescription = "Accept Request",
                                        tint = colorScheme.primary
                                    )
                                }

                                IconButton(
                                    modifier = Modifier.padding(end = 24.dp),
                                    onClick = { component.rejectRequest(it) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Reject Request",
                                        tint = colorScheme.onError
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}