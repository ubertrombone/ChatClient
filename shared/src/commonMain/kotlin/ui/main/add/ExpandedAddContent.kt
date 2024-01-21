package ui.main.add

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.AddComponent
import component.main.add.AddComponent.Config.BlockConfig
import component.main.add.AddComponent.Config.RequestConfig
import component.main.add.AddComponent.Slots.BlockList
import component.main.add.AddComponent.Slots.Requests
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.icons.BlockIcon
import ui.icons.SelectionIcon
import ui.main.add.block.BlockContent
import ui.main.add.requests.ExpandedRequestsContent

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpandedAddContent(component: AddComponent, modifier: Modifier = Modifier) {
    val slots by component.slots.subscribeAsState()

    val queryResult by component.query.subscribeAsState()
    val queryLoading by component.queryLoadingState.subscribeAsState()
    val queryStatus by component.queryStatus.subscribeAsState()
    val queryInput by component.queryInput.subscribeAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(slots) { if (slots.child?.instance == null) component.showSlot(RequestConfig) }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight().weight(5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            QueryField(
                queryInput = queryInput,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 4.dp),
                onValueChange = {
                    component.searchUsers(it)
                    component.updateInput(it)
                }
            )

            QueryResults(
                results = queryResult,
                loading = queryLoading,
                status = queryStatus,
                modifier = Modifier.fillMaxSize(),
                onClick = component::sendFriendRequest
            )
        }

        Divider(
            modifier = Modifier.fillMaxHeight().weight(.01f),
            thickness = 0.01.dp,
            color = colorScheme.scrim
        )

        Column(
            modifier = Modifier.fillMaxHeight().weight(5f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                SelectionIcon(
                    modifier = Modifier.padding(end = 8.dp),
                    color = if (slots.child?.instance is Requests) colorScheme.primaryContainer else colorScheme.background
                ) {
                    IconButton(onClick = { component.showSlot(RequestConfig) }) {
                        Icon(
                            painter = painterResource("person_alert.xml"),
                            contentDescription = "Go to requests",
                            tint = colorScheme.primary
                        )
                    }
                }

                SelectionIcon(
                    modifier = Modifier.padding(end = 8.dp),
                    color = if (slots.child?.instance is BlockList) colorScheme.primaryContainer else colorScheme.background
                ) { BlockIcon { component.showSlot(BlockConfig) } }
            }

            slots.child?.instance?.also {
                when (it) {
                    is BlockList -> BlockContent(it.component, Modifier.fillMaxSize())
                    is Requests -> ExpandedRequestsContent(it.component, snackbarHostState, Modifier.fillMaxSize())
                }
            }
        }
    }
}