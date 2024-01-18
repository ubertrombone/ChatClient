package ui.main.add.requests.received

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.received.ReceivedRequestsComponent
import io.ktor.client.statement.*
import kotlinx.collections.immutable.toImmutableList
import ui.composables.snackbarHelper
import ui.main.add.AddCard
import ui.main.add.requests.RequestSwitch
import util.Status.Error

@Composable
fun ReceivedContent(
    component: ReceivedRequestsComponent,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val requests by component.receivedList.subscribeAsState()
    val receivedStatus by component.listStatus.subscribeAsState()
    val receivedLoading by component.listLoading.subscribeAsState()
    val actionStatus by component.actionStatus.subscribeAsState()

    LaunchedEffect(actionStatus) {
        if (actionStatus is Error) snackbarHostState.snackbarHelper(
            message = ((actionStatus as Error).body as HttpResponse).bodyAsText()
        )
    }

    RequestSwitch(
        label = "You haven't received any requests.",
        results = requests.reqs.toImmutableList(),
        status = receivedStatus,
        loading = receivedLoading,
        modifier = modifier
    ) {
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