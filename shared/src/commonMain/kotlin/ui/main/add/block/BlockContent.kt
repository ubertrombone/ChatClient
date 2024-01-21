package ui.main.add.block

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.block.BlockComponent
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.main.add.AddCard
import ui.main.add.requests.RequestSwitch
import util.Status.Success

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BlockContent(component: BlockComponent, modifier: Modifier = Modifier) {
    val blockList by component.blockList.subscribeAsState()
    val status by component.listStatus.subscribeAsState()
    val loading by component.listLoading.subscribeAsState()
    val unblockStatus by component.actionStatus.subscribeAsState()

    LaunchedEffect(blockList) { component.getBlockList() }
    LaunchedEffect(unblockStatus) { if (unblockStatus == Success) component.getBlockList() }

    RequestSwitch(
        label = "You haven't blocked any users!",
        results = blockList.friends.toImmutableList(),
        status = status,
        loading = loading,
        modifier = modifier
    ) {
        AddCard(label = it.name, modifier = Modifier.fillMaxWidth()) {
            IconButton(modifier = Modifier.padding(end = 24.dp), onClick = { component.unblock(it) }) {
                Icon(
                    painter = painterResource("remove.xml"),
                    contentDescription = "Unblock ${it.name}",
                    tint = colorScheme.primary
                )
            }
        }
    }
}