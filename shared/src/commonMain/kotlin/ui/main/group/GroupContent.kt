package ui.main.group

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import component.main.group.GroupComponent
import util.SoftInputMode

@Composable
fun GroupContent(
    component: GroupComponent,
    modifier: Modifier = Modifier,
    showBottomNav: (Boolean) -> Unit
) {
    SoftInputMode(true)
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "GROUP CHAT VIEW",
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )
    }
}