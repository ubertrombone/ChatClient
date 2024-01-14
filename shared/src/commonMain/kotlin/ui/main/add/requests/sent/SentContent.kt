package ui.main.add.requests.sent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.sent.SentRequestsComponent
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.expect.ScrollLazyColumn
import ui.main.add.AddCard
import util.Status.Error
import util.Status.Success

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun SentContent(component: SentRequestsComponent, modifier: Modifier = Modifier) {
    val snackbarHostState = component.snackbarHostState
    val requests by component.sentList.subscribeAsState()
    val sentStatus by component.listStatus.subscribeAsState()
    val sentLoading by component.listLoading.subscribeAsState()
    val cancelStatus by component.actionStatus.subscribeAsState()

    // TODO: Test
    LaunchedEffect(requests) { component.getSentList() }
    LaunchedEffect(cancelStatus) { if (cancelStatus == Success) component.getSentList() }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(bottom = 12.dp),
                title = {},
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Return to search users",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 12.dp)
                            .size(40.dp)
                            .padding(10.dp)
                            .clip(CircleShape)
                            .clickable { component.dismiss() }
                    )
                },
                actions = {
                    Icon(
                        painter = painterResource("inbox.xml"),
                        contentDescription = "Sent Requests",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .padding(top = 12.dp, end = 12.dp)
                            .size(40.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .clickable { component.navBack() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = colorScheme.primaryContainer.copy(alpha = .7f), // TODO: Something wrong here in light mode
                    contentColor = colorScheme.onPrimaryContainer,
                    dismissActionContentColor = colorScheme.onPrimaryContainer
                )
            }
        },
        containerColor = colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when {
                sentLoading -> CircularProgressIndicator(
                    modifier = Modifier.size(120.dp).align(Alignment.Center),
                    color = colorScheme.primary,
                    trackColor = colorScheme.surfaceVariant
                )

                sentStatus is Error -> Text(
                    text = (sentStatus as Error).body.toString(),
                    modifier = Modifier.align(Alignment.Center)
                )

                sentStatus == Success -> {
                    if (requests.reqs.isEmpty()) Text(
                        text = "You haven't sent any requests.",
                        modifier = Modifier.align(Alignment.Center)
                    ) else ScrollLazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(requests.reqs.toImmutableList()) {
                            AddCard(
                                label = "Sent to ${it.toUsername}",
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    modifier = Modifier.padding(end = 24.dp),
                                    onClick = { component.cancelRequest(it.toUsername) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Cancel request",
                                        tint = colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}