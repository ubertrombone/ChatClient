package ui.main.add.requests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.RequestComponent
import component.main.add.requests.RequestComponent.Child.ReceivedChild
import component.main.add.requests.RequestComponent.Child.SentChild
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.expect.ScrollLazyColumn
import ui.icons.SentRequestsIcon
import ui.main.add.AddCard
import ui.main.add.requests.received.ReceivedContent
import util.Status

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpandedRequestsContent(
    component: RequestComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val childStack by component.childStack.subscribeAsState()

    Children(stack = childStack, modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (it.instance is ReceivedChild) colorScheme.primaryContainer else colorScheme.background,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    IconButton(onClick = { if (it.instance is SentChild) component.onReceivedRequestsSelected() }) {
                        Icon(
                            painter = painterResource("inbox.xml"),
                            contentDescription = "Received Requests",
                            tint = colorScheme.primary
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = if (it.instance is SentChild) colorScheme.primaryContainer else colorScheme.background,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    SentRequestsIcon {
                        if (it.instance is SentChild) component.onReceivedRequestsSelected()
                        else component.onSentRequestsSelected()
                    }
                }
            }

            Box(contentAlignment = Alignment.Center) {
                when (val child = it.instance) {
                    is ReceivedChild ->
                        ReceivedContent(component = child.component, snackbarHostState = snackbarHostState, modifier = Modifier.fillMaxSize())
                    is SentChild -> {
                        val requests by child.component.sentList.subscribeAsState()
                        val sentStatus by child.component.listStatus.subscribeAsState()
                        val sentLoading by child.component.listLoading.subscribeAsState()
                        val cancelStatus by child.component.actionStatus.subscribeAsState()

                        LaunchedEffect(requests) { child.component.getSentList() }
                        LaunchedEffect(cancelStatus) { if (cancelStatus == Status.Success) child.component.getSentList() }

                        Box(modifier = Modifier.fillMaxSize()) {
                            when {
                                sentLoading -> CircularProgressIndicator(
                                    modifier = Modifier.size(120.dp).align(Alignment.Center),
                                    color = colorScheme.primary,
                                    trackColor = colorScheme.surfaceVariant
                                )

                                sentStatus is Status.Error -> Text(
                                    text = (sentStatus as Status.Error).body.toString(),
                                    modifier = Modifier.align(Alignment.Center)
                                )

                                sentStatus == Status.Success -> {
                                    if (requests.reqs.isEmpty()) Text(
                                        text = "You haven't sent any requests.",
                                        modifier = Modifier.align(Alignment.Center)
                                    ) else ScrollLazyColumn(modifier = Modifier.fillMaxWidth()) {
                                        items(requests.reqs.toImmutableList()) {
                                            AddCard(
                                                label = "Sent to ${it.toUsername.name}",
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                IconButton(
                                                    modifier = Modifier.padding(end = 24.dp),
                                                    onClick = { child.component.cancelRequest(it.toUsername) }
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Delete,
                                                        contentDescription = "Cancel request",
                                                        tint = colorScheme.primary
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
            }
        }
    }
}