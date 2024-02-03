package ui.main.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import component.main.friends.chat.ChatComponent
import ui.icons.NavBackButton
import util.BottomBarSystemNavColor

@Composable
fun ChatWindow(component: ChatComponent, modifier: Modifier = Modifier) {
    val chat by component.messages.collectAsState(emptyList())

    BottomBarSystemNavColor(colorScheme.background)

    Box(modifier) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(50.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            NavBackButton { component.navBack() }
            Text(
                text = component.friend.username.name,
                style = typography.headlineLarge,
                color = colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 12.dp)
            )
            Icon(
                imageVector = Icons.Outlined.MoreVert,
                contentDescription = "Friend Profile",
                tint = colorScheme.primary,
                modifier = Modifier
                    .padding(end = 12.dp, top = 12.dp)
                    .size(40.dp)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .clickable { println("TODO") }
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(vertical = 50.dp).align(Alignment.Center).background(Color.Red)) {

        }

        Row(modifier = Modifier.fillMaxWidth().height(50.dp).align(Alignment.BottomCenter).background(Color.Cyan)) {

        }
    }
}