package ui.main.friends

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.friends.FriendsComponent
import kotlinx.coroutines.delay
import util.Status.Error

@Composable
fun CompactFriendsContent(component: FriendsComponent, modifier: Modifier = Modifier) {
    val status by component.status.subscribeAsState()
    val isLoading by component.isLoading.subscribeAsState()
    val friends by component.friends.subscribeAsState()
    val chatSlot by component.chatSlot.subscribeAsState()

    // TODO: Needs to be periodically refreshing
    LaunchedEffect(Unit) {
        delay(500)
        component.getFriends()
    }

    when {
        isLoading -> CircularProgressIndicator(
            modifier = Modifier.size(120.dp),
            color = colorScheme.primary,
            trackColor = colorScheme.surfaceVariant
        )

        status is Error -> Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = (status as Error).message,
                fontSize = typography.bodyLarge.fontSize,
                color = Color.DarkGray
            )
        }

        friends.friends.isEmpty() -> Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "You do not currently have any friends",
                fontSize = typography.bodyLarge.fontSize,
                color = Color.DarkGray
            )
        }

        else -> {
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
                modifier = modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                friendSelected = component::showChat
            )
        }
    }
}