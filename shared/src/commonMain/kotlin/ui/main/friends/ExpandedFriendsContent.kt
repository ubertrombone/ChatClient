package ui.main.friends

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.friends.FriendsComponent

@Composable
fun ExpandedFriendsContent(component: FriendsComponent, modifier: Modifier = Modifier) {
    val friends by component.friends.subscribeAsState()
    val chatSlot by component.chatSlot.subscribeAsState()

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        FriendsList(
            modifier = Modifier.fillMaxHeight().weight(1f).padding(8.dp),
            list = friends.friends,
            friendSelected = component::showChat
        )

        Divider(
            modifier = Modifier.fillMaxHeight().weight(.01f),
            thickness = 0.01.dp,
            color = colorScheme.scrim
        )

        Box(modifier = Modifier.fillMaxHeight().weight(4f).padding(8.dp)) {
            chatSlot.child?.instance?.let {
                Text(
                    text = it.friend.username.name,
                    fontSize = MaterialTheme.typography.displayLarge.fontSize,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = component::dismissChat,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
                }
            }
        }
    }
}