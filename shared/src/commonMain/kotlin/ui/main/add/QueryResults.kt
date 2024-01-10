package ui.main.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import component.main.add.model.Friends
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.expect.ScrollLazyColumn
import util.Status
import util.Status.*
import util.Username

@OptIn(ExperimentalResourceApi::class)
@Composable
fun QueryResults(
    results: Friends,
    loading: Boolean,
    status: Status,
    modifier: Modifier = Modifier,
    onClick: (Username) -> Unit
) {
    Box(modifier) {
        if (loading) CircularProgressIndicator(
            modifier = Modifier.size(120.dp).align(Alignment.Center),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        ) else {
            when (status) {
                is Error -> Text(
                    text = status.body.toString(),
                    modifier = Modifier.align(Alignment.Center)
                )
                Loading -> {}
                Success -> {
                    if (results.friends.isEmpty()) Text(
                        text = "No results found",
                        modifier = Modifier.align(Alignment.Center)
                    )
                    else ScrollLazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(items = results.friends.toImmutableList()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = it.name,
                                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(start = 24.dp)
                                )

                                IconButton(
                                    modifier = Modifier.padding(end = 24.dp),
                                    onClick = { onClick(it) }
                                ) {
                                    Icon(
                                        painter = painterResource("add_friend.xml"),
                                        contentDescription = "Send friend request to $it",
                                        tint = MaterialTheme.colorScheme.primary
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