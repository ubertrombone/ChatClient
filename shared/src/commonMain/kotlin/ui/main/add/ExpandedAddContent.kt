package ui.main.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.AddComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpandedAddContent(component: AddComponent, modifier: Modifier = Modifier) {
    val requestSlot by component.requestSlot.subscribeAsState()
    val blockSlot by component.blockSlot.subscribeAsState()

    val queryResult by component.query.subscribeAsState()
    val queryLoading by component.queryLoadingState.subscribeAsState()
    val queryStatus by component.queryStatus.subscribeAsState()
    val queryInput by component.queryInput.subscribeAsState()

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
                Surface(
                    shape = CircleShape,
                    color = requestSlot.child?.instance?.let { colorScheme.primaryContainer } ?: colorScheme.background,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    IconButton(onClick = {
                        if (requestSlot.child?.instance == null) component.showRequest()
                        if (blockSlot.child?.instance != null) component.dismissBlock()
                    }) {
                        Icon(
                            painter = painterResource("person_alert.xml"),
                            contentDescription = "Go to requests",
                            tint = colorScheme.primary,
                            modifier = Modifier
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = blockSlot.child?.instance?.let { colorScheme.primaryContainer } ?: colorScheme.background,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    IconButton(onClick = {
                        blockSlot.child?.instance?.let {
                            component.dismissBlock()
                            component.showRequest()
                        } ?: run {
                            component.showBlock()
                            component.dismissRequest()
                        }
                    }) {
                        Icon(
                            painter = painterResource("block.xml"),
                            contentDescription = "Go to block list",
                            tint = colorScheme.primary
                        )
                    }
                }
            }

            requestSlot.child?.instance?.also {
                // TODO: Add request content, figure it out
            }
            blockSlot.child?.instance?.also {
                // TODO: Figure it out
            }
        }
    }
}