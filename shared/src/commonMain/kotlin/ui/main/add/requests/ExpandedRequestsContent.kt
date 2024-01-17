package ui.main.add.requests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpandedRequestsContent(component: RequestComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()

    Box(modifier) {
        Children(childStack) {
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
                    IconButton(onClick = {
                        if (it.instance is SentChild) component.onReceivedRequestsSelected()
                        else component.onSentRequestsSelected()
                    }) {
                        Icon(
                            painter = painterResource("outgoing.xml"),
                            contentDescription = "Sent Requests",
                            tint = colorScheme.primary
                        )
                    }
                }
            }

            Box(Modifier.fillMaxSize(), Alignment.Center) {
                when (val child = it.instance) {
                    is ReceivedChild -> Text("REQUESTS")
                    is SentChild -> Text("SENT")
                }
            }
        }
    }
}