package ui.main.friends

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.friends.FriendsComponent

@Composable
fun CompactFriendsContent(
    component: FriendsComponent,
    modifier: Modifier = Modifier,
    showBottomNav: (Boolean) -> Unit
) {
    val friends by component.friends.subscribeAsState()
    val chatSlot by component.chatSlot.subscribeAsState()

    chatSlot.child?.instance?.let {
        showBottomNav(false)
        ChatWindow(component = it, modifier = Modifier.fillMaxSize())
    } ?: FriendsList(
        list = friends.friends,
        modifier = modifier.padding(vertical = 12.dp),
        friendSelected = component::showChat
    ).also { showBottomNav(true) }
}