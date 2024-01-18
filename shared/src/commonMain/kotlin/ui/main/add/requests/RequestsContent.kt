package ui.main.add.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.add.requests.RequestComponent
import component.main.add.requests.RequestComponent.Child.ReceivedChild
import component.main.add.requests.RequestComponent.Child.SentChild
import ui.main.add.requests.received.CompactReceivedContent
import ui.main.add.requests.sent.CompactSentContent

@Composable
fun RequestsContent(component: RequestComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()

    Box(modifier) {
        Children(childStack) {
            when (val child = it.instance) {
                is ReceivedChild -> CompactReceivedContent(component = child.component, modifier = Modifier.fillMaxSize())
                is SentChild -> CompactSentContent(component = child.component, modifier = Modifier.fillMaxSize())
            }
        }
    }
}