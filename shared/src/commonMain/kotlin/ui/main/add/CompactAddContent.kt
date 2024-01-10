package ui.main.add

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.AddComponent
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import ui.composables.expect.ScrollLazyColumn
import ui.composables.states.rememberStatusAuthenticationFieldState
import util.Constants.REQUIREMENT_MAX
import util.Status.*
import util.textFieldColors

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CompactAddContent(component: AddComponent, modifier: Modifier = Modifier) {
    val queryResult by component.query.subscribeAsState()
    val queryLoading by component.queryLoadingState.subscribeAsState()
    val queryStatus by component.queryStatus.subscribeAsState()
    val queryState = rememberStatusAuthenticationFieldState(input = "")
    val queryInput by queryState.input.collectAsState()
    val input by remember(queryInput) { mutableStateOf(queryInput) }

    // TODO:
    //  1. Top row: top right, icon buttons to nav to requests and block list
    //  2. Text field: Single line, limited by max username chars
    //  3. Screen blank until search is done, then a simple list of usernames as cards with a text button next to each "Send Request" or a an icon let's see

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

        OutlinedTextField(
            value = input,
            onValueChange = {
                if (it.length <= REQUIREMENT_MAX) {
                    component.searchUsers(it)
                    queryState.updateInput(it)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 4.dp),
            placeholder = { Text(text = "Search for Friends") },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(autoCorrect = false, imeAction = ImeAction.Done),
            colors = textFieldColors(),
            shape = RoundedCornerShape(36.dp)
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (queryLoading) CircularProgressIndicator(
                modifier = Modifier.size(120.dp).align(Alignment.Center),
                color = colorScheme.primary,
                trackColor = colorScheme.surfaceVariant
            ) else {
                when (queryStatus) {
                    is Error -> Text(
                        text = (queryStatus as Error).body.toString(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Loading -> {}
                    Success -> {
                        if (queryResult.friends.isEmpty()) Text(
                            text = "No results found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                        else ScrollLazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(items = queryResult.friends.toImmutableList()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = it.name,
                                        fontSize = typography.bodyLarge.fontSize,
                                        color = colorScheme.primary,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )

                                    IconButton(
                                        modifier = Modifier.padding(end = 8.dp),
                                        onClick = { component.sendFriendRequest(it) }
                                    ) {
                                        Icon(
                                            painter = painterResource("add_friend.xml"),
                                            contentDescription = "Send friend request to $it",
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
}