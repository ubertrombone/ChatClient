package ui.main.add

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import ui.composables.states.rememberStatusAuthenticationFieldState

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CompactAddContent(component: AddComponent, modifier: Modifier = Modifier) {
    val queryResult by component.query.subscribeAsState()
    val queryLoading by component.queryLoadingState.subscribeAsState()
    val queryStatus by component.queryStatus.subscribeAsState()
    val queryState = rememberStatusAuthenticationFieldState(input = "")

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = component::showRequest) {
                Icon(
                    painter = painterResource("person_alert.xml"),
                    contentDescription = "Go to requests",
                    tint = colorScheme.primary
                )
            }

            IconButton(onClick = component::showBlock, modifier = Modifier.padding(start = 8.dp)) {
                Icon(
                    painter = painterResource("block.xml"),
                    contentDescription = "Go to block list",
                    tint = colorScheme.primary
                )
            }
        }

        QueryField(
            state = queryState,
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 4.dp),
            onValueChange = component::searchUsers
        )

        QueryResults(
            results = queryResult,
            loading = queryLoading,
            status = queryStatus,
            modifier = Modifier.fillMaxSize(),
            onClick = component::sendFriendRequest
        )
    }
}