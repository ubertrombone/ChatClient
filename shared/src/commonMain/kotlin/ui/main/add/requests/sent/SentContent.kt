package ui.main.add.requests.sent

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.sent.SentRequestsComponent
import kotlinx.collections.immutable.toImmutableList
import ui.main.add.AddCard
import ui.main.add.requests.RequestSwitch
import util.Status.Success

@Composable
fun SentContent(component: SentRequestsComponent, modifier: Modifier = Modifier) {
    val requests by component.sentList.subscribeAsState()
    val sentStatus by component.listStatus.subscribeAsState()
    val sentLoading by component.listLoading.subscribeAsState()
    val cancelStatus by component.actionStatus.subscribeAsState()

    LaunchedEffect(requests) { component.getSentList() }
    LaunchedEffect(cancelStatus) { if (cancelStatus == Success) component.getSentList() }

    RequestSwitch(
        label = "You haven't sent any requests.",
        results = requests.reqs.toImmutableList(),
        status = sentStatus,
        loading = sentLoading,
        modifier = modifier
    ) {
        AddCard(
            label = "Sent to ${it.toUsername.name}",
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.padding(end = 24.dp),
                onClick = { component.cancelRequest(it.toUsername) }
            ) {
                Icon(
                    imageVector = Default.Delete,
                    contentDescription = "Cancel request",
                    tint = colorScheme.primary
                )
            }
        }
    }
}