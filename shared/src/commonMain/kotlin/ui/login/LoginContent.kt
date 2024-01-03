package ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.model.AuthenticationRequest
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.login.LoginComponent
import io.ktor.client.statement.*
import kotlinx.coroutines.delay
import ui.composables.states.rememberLoginAuthenticationFieldState
import util.BottomBarSystemNavColor
import util.Constants
import util.Constants.INVALID_USERNAME
import util.Constants.NO_PASSWORD_PROVIDED
import util.MainPhases.REGISTER
import util.Status
import util.Status.Error
import util.Status.Success
import util.toUsername

@Suppress("DuplicatedCode")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(component: LoginComponent, modifier: Modifier = Modifier) {
    val initStatus by component.initStatus.subscribeAsState()
    val isInitLoading by component.isInitLoading.subscribeAsState()
    val loginStatus by component.loginStatus.subscribeAsState()
    val isLoading by component.isLoading.subscribeAsState()
    val rememberMe by component.rememberMe.subscribeAsState()
    val usernameState = rememberLoginAuthenticationFieldState(
        initialInput = if (rememberMe) component.settings.username.get() else ""
    )
    val passwordState = rememberLoginAuthenticationFieldState(
        initialInput = if (rememberMe) component.settings.password.get() else ""
    )

    LaunchedEffect(Unit) {
        // Prevents loading bar from looking like a screen flicker
        delay(500)
        component.initLogin()
    }
    BottomBarSystemNavColor(colorScheme.background)

    Scaffold(
        modifier = modifier,
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
                    IconButton(
                        modifier = Modifier.padding(end = 24.dp),
                        onClick = { component.pushTo(REGISTER) }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Register"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary,
                    actionIconContentColor = colorScheme.primary
                )
            )
        },
        containerColor = colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(it)) {
            if (isInitLoading) LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp).align(Alignment.Center),
                color = colorScheme.primary,
                trackColor = colorScheme.surfaceVariant
            ) else {
                when (initStatus) {
                    is Error -> Text(
                        text = ((initStatus as Error).body as HttpResponse).status.description,
                        fontSize = typography.bodyLarge.fontSize,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    Status.Loading -> { /* Loading is handled by isInitLoading and isLoading */ }
                    Success -> {
                        LoginForm(
                            modifier = Modifier.align(Alignment.Center),
                            usernameState = usernameState,
                            passwordState = passwordState,
                            rememberMe = rememberMe,
                            isLoading = isLoading,
                            loginStatus = loginStatus,
                            onCheckChange = component::update
                        ) {
                            when {
                                usernameState.input.value.length < Constants.USERNAME_REQUIREMENT_MIN ->
                                    component.updateLogin(Error(INVALID_USERNAME))
                                passwordState.input.value.isBlank() -> component.updateLogin(Error(NO_PASSWORD_PROVIDED))
                                else -> component.login(
                                    credentials = AuthenticationRequest(
                                        username = usernameState.input.value.toUsername(),
                                        password = passwordState.input.value
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}