package ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.login.LoginComponent
import ui.composables.PasswordField
import ui.composables.UsernameField
import ui.composables.states.rememberLoginAuthenticationFieldState
import util.MainPhases.REGISTER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(component: LoginComponent, modifier: Modifier = Modifier) {
    val status by component.status.subscribeAsState()
    val username by component.username.subscribeAsState()
    val usernamestate = rememberLoginAuthenticationFieldState(username)
    val passwordState = rememberLoginAuthenticationFieldState(initialInput = "")

    LaunchedEffect(status) {
        //component.update(component.server.login())
    }

    Scaffold(
        modifier = modifier.padding(top = 24.dp),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = component.title,
                        fontSize = typography.displayLarge.fontSize,
                        fontWeight = typography.displayLarge.fontWeight,
                    )
                },
                actions = {
                    Text(
                        text = "Register",
                        fontSize = typography.bodyMedium.fontSize,
                        fontWeight = typography.bodyMedium.fontWeight,
                        modifier = Modifier
                            .padding(end = 24.dp)
                            .clickable { component.pushTo(REGISTER) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        containerColor = colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UsernameField(modifier = Modifier.width(300.dp), state = usernamestate)

            Spacer(Modifier.height(50.dp))

            PasswordField(modifier = Modifier.width(300.dp), state = passwordState)

            // TODO: Login Button
            // TODO: Register Button impl
        }
    }
}