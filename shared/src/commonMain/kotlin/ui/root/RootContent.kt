package ui.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import component.root.RootComponent

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text("Chat", Modifier.align(Alignment.Center))
    }
}