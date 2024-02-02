package ui.main.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.router.stack.ChildStack
import component.main.MainComponent
import component.main.MainComponent.Child.*
import ui.main.add.AddContent
import ui.main.friends.FriendsContent
import ui.main.group.GroupContent

@Composable
fun ChildrenBox(
    childStack: ChildStack<*, MainComponent.Child>,
    modifier: Modifier = Modifier,
    showBottomNav: (Boolean) -> Unit = { true }
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Children(stack = childStack) {
            when (val child = it.instance) {
                is AddChild ->
                    AddContent(component = child.component, modifier = Modifier.fillMaxSize())
                        .also { showBottomNav(true) }
                is FriendsChild -> FriendsContent(
                    component = child.component,
                    modifier = Modifier.fillMaxSize(),
                    showBottomNav = showBottomNav
                )
                is GroupChild -> GroupContent(component = child.component, modifier = Modifier.fillMaxSize())
            }
        }
    }
}