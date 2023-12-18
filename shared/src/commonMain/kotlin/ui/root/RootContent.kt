package ui.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.root.RootComponent
import component.root.RootComponent.Child
import component.root.RootComponent.Child.*
import ui.login.LoginContent

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    // TODO: Root screens:
    //  1. Login -- Default screen for when user does not have valid authentication token
    //      - Contains:
    //          1. Loading view -- on app start, app will need to communicate with server to determine if user is authenticated
    //          2. Login form -- if user isn't authenticated, they need to login
    //          3. Link to register -- for new users
    //  2. Register -- Form screen where user will create a username and password (with confirm field)
    //  3. Main/FriendsList -- If user is authenticated, they will be directed to the main view.
    //      - Contains:
    //          1. List of chats or online friends?
    //          2. Top App Bar with useful information like app/view name? Logout?
    //          3. Bottom App Bar with nav to Chats/Groups/Settings

    val childStack by component.childStack.subscribeAsState()

    Box(modifier = modifier) {
        Children(stack = childStack) {
            when (val child = it.instance) {
                is LoginChild -> LoginContent(component = child.component, modifier = Modifier.fillMaxSize())
                is MainChild -> TODO()
                is RegisterChild -> TODO()
            }
        }
    }
}