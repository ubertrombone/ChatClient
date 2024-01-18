package ui.main.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.AddComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.icons.BlockIcon
import ui.main.add.requests.RequestsContent

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CompactAddContent(component: AddComponent, modifier: Modifier = Modifier) {
    val requestSlot by component.requestSlot.subscribeAsState()
    val blockSlot by component.blockSlot.subscribeAsState()

    val queryResult by component.query.subscribeAsState()
    val queryLoading by component.queryLoadingState.subscribeAsState()
    val queryStatus by component.queryStatus.subscribeAsState()
    val queryInput by component.queryInput.subscribeAsState()
    val requests by component.receiveListModel.result.subscribeAsState()

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            BadgedBox(
                modifier = Modifier
                    .padding(20.dp)
                    .clickable { component.showRequest() },
                badge = { if (requests.reqs.isNotEmpty()) Badge { Text(text = "${requests.reqs.size}") } }
            ) {
                Icon(
                    painter = painterResource("person_alert.xml"),
                    contentDescription = "Go to requests",
                    tint = colorScheme.primary,
                    modifier = Modifier.padding(10.dp)
                )
            }

            BlockIcon(modifier = Modifier.padding(start = 8.dp), onClick = component::showBlock)
        }

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

    requestSlot.child?.instance?.also { RequestsContent(component = it, modifier = modifier) }
}