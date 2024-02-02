package ui.main.friends

import androidx.compose.foundation.layout.Box
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
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import util.SoftInputMode
import util.Status.Error

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun FriendsContent(
    component: FriendsComponent,
    modifier: Modifier = Modifier,
    showBottomNav: (Boolean) -> Unit
) {
    val windowSizeClass = calculateWindowSizeClass()
    val status by component.status.subscribeAsState()
    val isLoading by component.isLoading.subscribeAsState()
    val friends by component.friends.subscribeAsState()

    LaunchedEffect(status) {
        status.takeIf { status is Error }?.let {
            if ((status as Error).body.toString() == Unauthorized.description) component.logout()
        }
    }

    SoftInputMode(true)

    when {
        isLoading && friends.friends.isEmpty() -> CircularProgressIndicator(
            modifier = Modifier.size(120.dp),
            color = colorScheme.primary,
            trackColor = colorScheme.surfaceVariant
        )

        status is Error -> Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Text(
                text = (status as Error).body.toString(),
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
                ExpandedFriendsContent(component, modifier)
            else CompactFriendsContent(component, modifier, showBottomNav)
    }
}