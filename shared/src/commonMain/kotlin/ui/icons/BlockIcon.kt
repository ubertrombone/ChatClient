package ui.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chatclient.shared.generated.resources.Res
import chatclient.shared.generated.resources.block
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BlockIcon(modifier: Modifier = Modifier, onClick: () -> Unit) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(
            painter = painterResource(Res.drawable.block),
            contentDescription = "Go to block list",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}