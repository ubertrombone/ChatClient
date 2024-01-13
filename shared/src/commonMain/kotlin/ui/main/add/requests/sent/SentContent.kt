package ui.main.add.requests.sent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import component.main.add.requests.sent.SentRequestsComponent
import kotlinx.collections.immutable.toImmutableList
import ui.composables.expect.ScrollLazyColumn
import ui.main.add.AddCard
import util.Status.Error
import util.Status.Success

@Composable
fun SentContent(component: SentRequestsComponent, modifier: Modifier = Modifier) {
    val requests by component.sentList.subscribeAsState()
    val sentStatus by component.listStatus.subscribeAsState()
    val sentLoading by component.listLoading.subscribeAsState()
    val cancelStatus by component.actionStatus.subscribeAsState()

    // TODO: Test
    LaunchedEffect(requests) { component.getSentList() }
    LaunchedEffect(cancelStatus) { if (cancelStatus == Success) component.getSentList() }

    Box(modifier = modifier) {
        when {
            sentLoading -> CircularProgressIndicator(
                modifier = Modifier.size(120.dp).align(Alignment.Center),
                color = colorScheme.primary,
                trackColor = colorScheme.surfaceVariant
            )

            sentStatus is Error -> Text(
                text = (sentStatus as Error).body.toString(),
                modifier = Modifier.align(Alignment.Center)
            )

            sentStatus == Success -> {
                if (requests.reqs.isEmpty()) Text(
                    text = "You haven't sent any requests.",
                    modifier = Modifier.align(Alignment.Center)
                ) else ScrollLazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(requests.reqs.toImmutableList()) {
                        AddCard(
                            label = "Sent to ${it.toUsername}",
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            IconButton(
                                modifier = Modifier.padding(end = 24.dp),
                                onClick = { component.cancelRequest(it.toUsername) }
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