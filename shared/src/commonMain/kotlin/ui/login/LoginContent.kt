package ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.login.LoginComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(component: LoginComponent, modifier: Modifier = Modifier) {
    val status by component.status.subscribeAsState()
    
    Column(modifier = modifier) {
        TopAppBar(
            title = { Text(component.title) },
            actions = { TODO() }
        )
    }
}