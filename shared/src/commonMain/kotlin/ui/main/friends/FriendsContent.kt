package ui.main.friends

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
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
import util.Status

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun FriendsContent(component: FriendsComponent, modifier: Modifier = Modifier) {
    val windowSizeClass = calculateWindowSizeClass()
    val status by component.status.subscribeAsState()
    val isLoading by component.isLoading.subscribeAsState()
    val friends by component.friends.subscribeAsState()

    // TODO: Needs to be periodically refreshing
    LaunchedEffect(Unit) {
        delay(500)
        component.getFriends()
    }

    when {
        isLoading && friends.friends.isEmpty() -> CircularProgressIndicator(
            modifier = Modifier.size(120.dp),
            color = colorScheme.primary,
            trackColor = colorScheme.surfaceVariant
        )

        status is Status.Error -> Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = (status as Status.Error).message,
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

        else ->
            if (windowSizeClass.widthSizeClass == Expanded && windowSizeClass.heightSizeClass != Compact)
                ExpandedFriendsContent(component, modifier.padding(top = 12.dp, start = 12.dp))
            else CompactFriendsContent(component, modifier)
    }
}