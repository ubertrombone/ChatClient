package ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.router.stack.ChildStack
import component.main.MainComponent
import component.main.MainComponent.Child.*
import ui.main.add.AddContent
import ui.main.chat.ChatContent
import ui.main.group.GroupContent

@Composable
fun ChildrenBox(childStack: ChildStack<*, MainComponent.Child>, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Children(stack = childStack) {
            when (val child = it.instance) {
                is AddChild -> AddContent(component = child.component, modifier = Modifier.fillMaxSize())
                is ChatChild -> ChatContent(component = child.component, modifier = Modifier.fillMaxSize())
                is GroupChild -> GroupContent(component = child.component, modifier = Modifier.fillMaxSize())
            }
        }
    }
}