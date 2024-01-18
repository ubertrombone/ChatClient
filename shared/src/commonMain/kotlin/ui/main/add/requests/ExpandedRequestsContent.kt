package ui.main.add.requests

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.RequestComponent
import component.main.add.requests.RequestComponent.Child.ReceivedChild
import component.main.add.requests.RequestComponent.Child.SentChild
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.icons.ReceivedRequestsIcon
import ui.icons.SelectionIcon
import ui.icons.SentRequestsIcon
import ui.main.add.requests.received.ReceivedContent
import ui.main.add.requests.sent.SentContent

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
                SelectionIcon(
                    modifier = Modifier.padding(end = 8.dp),
                    color = if (it.instance is ReceivedChild) colorScheme.primaryContainer else colorScheme.background
                ) { ReceivedRequestsIcon { if (it.instance is SentChild) component.onReceivedRequestsSelected() } }

                SelectionIcon(
                    modifier = Modifier.padding(end = 8.dp),
                    color = if (it.instance is SentChild) colorScheme.primaryContainer else colorScheme.background
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
                    is SentChild -> SentContent(component = child.component, modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}