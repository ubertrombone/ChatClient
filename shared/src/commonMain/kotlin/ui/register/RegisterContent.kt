package ui.register

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.register.RegisterComponent
import ui.composables.NavBackButton
import ui.composables.states.rememberRegisterAuthenticationFieldState
import util.MainPhases.LOGIN

@Suppress("DuplicatedCode")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterContent(component: RegisterComponent, modifier: Modifier = Modifier) {
    val isLoading by component.isLoading.subscribeAsState()
    val registrationStatus by component.registrationStatus.subscribeAsState()
    val usernameStatus by component.usernameStatus.subscribeAsState()
    val passwordStatus by component.passwordStatus.subscribeAsState()
    val rememberMe by component.rememberMe.subscribeAsState()
    val username = rememberRegisterAuthenticationFieldState(input = "")
    val password = rememberRegisterAuthenticationFieldState(input = "")
    val confirmPassword = rememberRegisterAuthenticationFieldState(input = "")

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
                navigationIcon = { NavBackButton { component.pushTo(LOGIN) } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        containerColor = colorScheme.background
    ) {
        RegisterForm(
            modifier = Modifier.fillMaxSize().padding(it),
            usernameState = username,
            passwordState = password,
            confirmPasswordState = confirmPassword,
            rememberMe = rememberMe,
            registerStatus = registrationStatus,
            usernameStatus = usernameStatus,
            passwordStatus = passwordStatus,
            isLoading = isLoading,
            onCheckChange = component::update
        ) {
            component.validateCredentials(
                username = username.input.value,
                password = password.input.value,
                confirmation = confirmPassword.input.value
            )
        }
    }
}