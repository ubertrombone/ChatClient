package ui.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.root.RootComponent
import component.root.RootComponent.Child.*
import ui.login.LoginContent
import ui.main.MainContent
import ui.register.RegisterContent

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    val childStack by component.childStack.subscribeAsState()

    Box(modifier = modifier) {
        Children(stack = childStack) {
            when (val child = it.instance) {
                is LoginChild -> LoginContent(component = child.component, modifier = Modifier.fillMaxSize())
                is MainChild -> MainContent(component = child.component, modifier = Modifier.fillMaxSize())
                is RegisterChild -> RegisterContent(component = child.component, modifier = Modifier.fillMaxSize())
            }
        }
    }
}