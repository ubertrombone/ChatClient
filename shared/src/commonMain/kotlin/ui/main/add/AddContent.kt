package ui.main.add

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import component.main.add.AddComponent

@Composable
fun AddContent(component: AddComponent, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            text = "ADD VIEW",
            fontSize = MaterialTheme.typography.displayLarge.fontSize
        )
    }
}