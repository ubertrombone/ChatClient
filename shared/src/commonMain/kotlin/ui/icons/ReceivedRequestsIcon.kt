package ui.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ReceivedRequestsIcon(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource("inbox.xml"),
            contentDescription = "Received Requests",
            tint = colorScheme.primary
        )
    }
}