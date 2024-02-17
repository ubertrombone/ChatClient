package ui.main.friends

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.VerticalDivider
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
        horizontalArrangement = Arrangement.Center
    ) {
        FriendsList(
            modifier = Modifier.fillMaxHeight().weight(2f),
            list = friends.friends,
            friendSelected = component::showChat
        )

        VerticalDivider(
            modifier = Modifier.fillMaxHeight().weight(.01f),
            thickness = 0.01.dp,
            color = colorScheme.scrim
        )

        Box(modifier = Modifier.fillMaxHeight().weight(3f)) {
            chatSlot.child?.instance?.let { ChatWindow(it, Modifier.fillMaxSize()) }
        }
    }
}