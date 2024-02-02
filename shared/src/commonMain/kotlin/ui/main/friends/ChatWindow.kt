package ui.main.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import component.main.friends.chat.ChatComponent
import util.BottomBarSystemNavColor

@Composable
fun ChatWindow(component: ChatComponent, modifier: Modifier = Modifier) {
    val chat by component.messages.collectAsState(emptyList())

    BottomBarSystemNavColor(colorScheme.background)

    Box(modifier) {
        Row(modifier = Modifier.fillMaxWidth().height(36.dp).align(Alignment.TopCenter).background(Color.Green)) {

        }

        Column(modifier = Modifier.fillMaxSize().padding(vertical = 36.dp).align(Alignment.Center).background(Color.Red)) {

        }

        Row(modifier = Modifier.fillMaxWidth().height(36.dp).align(Alignment.BottomCenter).background(Color.Cyan)) {

        }
    }
}