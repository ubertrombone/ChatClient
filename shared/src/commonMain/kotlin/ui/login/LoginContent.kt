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
import ui.components.PasswordField
import ui.components.UsernameField
import util.MainPhases.REGISTER

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(component: LoginComponent, modifier: Modifier = Modifier) {
    val status by component.status.subscribeAsState()
    val username by component.username.subscribeAsState()
    var usernameInput by remember(username) { mutableStateOf(username) }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf(false) }

    LaunchedEffect(status) {
        component.update(component.server.login())
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = component.title,
                        fontSize = typography.headlineLarge.fontSize,
                        fontWeight = typography.headlineLarge.fontWeight
                    )
                },
                actions = {
                    Text(
                        text = "Register",
                        fontSize = typography.bodyMedium.fontSize,
                        fontWeight = typography.bodyMedium.fontWeight,
                        modifier = Modifier.clickable { component.pushTo(REGISTER) }
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
            UsernameField(
                usernameInput = usernameInput,
                isError = error,
                modifier = Modifier.width(200.dp).height(200.dp),
            ) { input ->
                usernameInput = input
                component.update(input)
            }
        }

        PasswordField(
            password = password,
            isError = error,
            modifier = Modifier.width(200.dp).height(200.dp),
            onValueChange = { input -> password = input }
        )
    }
}