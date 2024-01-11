package ui.main.add

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.AddComponent

@Composable
fun ExpandedAddContent(component: AddComponent, modifier: Modifier = Modifier) {
    val queryResult by component.query.subscribeAsState()
    val queryLoading by component.queryLoadingState.subscribeAsState()
    val queryStatus by component.queryStatus.subscribeAsState()
    val queryInput by component.queryInput.subscribeAsState()
// TODO: Two panes: Left-hand pane is search bar with icon to show blocked list; right-hand pane is Requests

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
            color = MaterialTheme.colorScheme.scrim
        )
    }
}