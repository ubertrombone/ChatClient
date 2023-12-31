package ui.main.friends

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import component.main.friends.FriendsComponent

@Composable
fun FriendsContent(component: FriendsComponent, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "CHAT VIEW",
            fontSize = typography.displayLarge.fontSize
        )
    }
}