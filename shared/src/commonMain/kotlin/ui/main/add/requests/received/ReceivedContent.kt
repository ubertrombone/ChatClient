package ui.main.add.requests.received

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.received.ReceivedRequestsComponent
import kotlinx.collections.immutable.toImmutableList
import ui.composables.expect.ScrollLazyColumn
import ui.main.add.AddCard
import util.Status.Error
import util.Status.Success

@Composable
fun ReceivedContent(component: ReceivedRequestsComponent, modifier: Modifier = Modifier) {
    val requests by component.receivedList.subscribeAsState()
    val receivedStatus by component.listStatus.subscribeAsState()
    val receivedLoading by component.listLoading.subscribeAsState()
    val actionStatus by component.actionStatus.subscribeAsState()

    LaunchedEffect(requests) { component.getRequests() }
    LaunchedEffect(actionStatus) { if (actionStatus == Success) component.getRequests() }

    Box(modifier = modifier) {
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