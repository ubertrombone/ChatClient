package ui.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.register.RegisterComponent
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
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Return to Login",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 12.dp)
                            .size(40.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .clickable { component.pushTo(LOGIN) }
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