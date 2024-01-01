package ui.main.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            modifier = Modifier.fillMaxHeight().weight(1f).padding(8.dp).background(Color.Cyan),
            list = friends.friends,
            friendSelected = component::showChat
        )

        Box(modifier = Modifier.fillMaxHeight().weight(4f).padding(8.dp).background(Color.Green)) {
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