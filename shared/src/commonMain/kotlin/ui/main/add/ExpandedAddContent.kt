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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.icons.BlockIcon
import ui.icons.SelectionIcon
import ui.main.add.block.BlockContent
import ui.main.add.requests.ExpandedRequestsContent

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpandedAddContent(component: AddComponent, modifier: Modifier = Modifier) {
    val requestSlot by component.requestSlot.subscribeAsState()
    val blockSlot by component.blockSlot.subscribeAsState()

    val queryResult by component.query.subscribeAsState()
    val queryLoading by component.queryLoadingState.subscribeAsState()
    val queryStatus by component.queryStatus.subscribeAsState()
    val queryInput by component.queryInput.subscribeAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(requestSlot) {
        if (requestSlot.child?.instance == null && blockSlot.child?.instance == null) component.showRequest()
    }

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
                    color = requestSlot.child?.instance?.let { colorScheme.primaryContainer } ?: colorScheme.background
                ) {
                    IconButton(onClick = {
                        if (requestSlot.child?.instance == null) component.showRequest()
                        if (blockSlot.child?.instance != null) component.dismissBlock()
                    }) {
                        Icon(
                            painter = painterResource("person_alert.xml"),
                            contentDescription = "Go to requests",
                            tint = colorScheme.primary
                        )
                    }
                }

                SelectionIcon(
                    modifier = Modifier.padding(end = 8.dp),
                    color = blockSlot.child?.instance?.let { colorScheme.primaryContainer } ?: colorScheme.background
                ) {
                    BlockIcon {
                        blockSlot.child?.instance?.let {
                            component.dismissBlock()
                            component.showRequest()
                        } ?: run {
                            component.showBlock()
                            component.dismissRequest()
                        }
                    }
                }
            }

            requestSlot.child?.instance?.also { ExpandedRequestsContent(it, snackbarHostState, Modifier.fillMaxSize()) }
            blockSlot.child?.instance?.also { BlockContent(it, Modifier.fillMaxSize()) }
        }
    }
}