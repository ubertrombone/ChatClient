package ui.main.friends

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.friends.FriendsComponent

@Composable
fun CompactFriendsContent(component: FriendsComponent, modifier: Modifier = Modifier) {
    val friends by component.friends.subscribeAsState()
    val chatSlot by component.chatSlot.subscribeAsState()

    chatSlot.child?.instance?.let {
        Box(modifier = modifier) {
            Text(
                text = it.friend.username.name,
                fontSize = typography.displayLarge.fontSize,
                modifier = Modifier.align(Alignment.Center)
            )

            IconButton(
                onClick = component::dismissChat,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go back")
            }
        }
    } ?: FriendsList(
        list = friends.friends,
        modifier = modifier.padding(vertical = 12.dp),
        friendSelected = component::showChat
    )
}