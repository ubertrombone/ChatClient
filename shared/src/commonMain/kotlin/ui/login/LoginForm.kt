package ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.ktor.client.statement.*
import ui.composables.PasswordField
import ui.composables.RememberMeCheckbox
import ui.composables.UsernameField
import ui.composables.expect.ScrollLazyColumn
import ui.composables.states.LoginAuthenticationFieldState
import ui.composables.states.Types.PASSWORD
import ui.composables.states.Types.USERNAME
import util.Constants.INVALID_USERNAME
import util.Constants.NO_PASSWORD_PROVIDED
import util.Status
import util.Status.Error
import util.Status.Success

@Composable
fun LoginForm(
    usernameState: LoginAuthenticationFieldState,
    passwordState: LoginAuthenticationFieldState,
    rememberMe: Boolean,
    modifier: Modifier = Modifier,
    loginStatus: Status = Success,
    isLoading: Boolean = false,
    onCheckChange: (Boolean) -> Unit,
    onButtonClick: () -> Unit
) {
    var errorMessage by remember { mutableStateOf<String?>(null) }

    usernameState.validateInput(loginStatus, type = USERNAME)
    passwordState.validateInput(loginStatus, type = PASSWORD)

    LaunchedEffect(loginStatus) {
        errorMessage = runCatching { (loginStatus as Error<*>).body as HttpResponse }.getOrNull()?.bodyAsText()
    }

    ScrollLazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = errorMessage?.let {
                    when (it) {
                        NO_PASSWORD_PROVIDED -> ""
                        INVALID_USERNAME -> ""
                        else -> it
                    }
                } ?: "",
                color = errorMessage?.let {
                    when (it) {
                        NO_PASSWORD_PROVIDED -> colorScheme.background
                        INVALID_USERNAME -> colorScheme.background
                        else -> colorScheme.error
                    }
                } ?: colorScheme.background,
                fontSize = typography.bodyLarge.fontSize,
                fontWeight = typography.bodyLarge.fontWeight
            )

            Spacer(Modifier.height(12.dp))
        }

        item {
            UsernameField(
                state = usernameState,
                enabled = !isLoading,
                modifier = Modifier.width(300.dp),
                label = errorMessage?.takeIf { it == INVALID_USERNAME } ?: "Username"
            )

            Spacer(Modifier.height(24.dp))
        }

        item {
            PasswordField(
                state = passwordState,
                enabled = !isLoading,
                modifier = Modifier.width(300.dp),
                label = errorMessage?.takeIf { it == NO_PASSWORD_PROVIDED } ?: "Password",
                hasTrailingIcon = false
            )

            Spacer(Modifier.height(8.dp))
        }

        item {
            RememberMeCheckbox(
                rememberMe = rememberMe,
                enabled = !isLoading,
                onCheckChanged = onCheckChange,
                modifier = Modifier.width(300.dp)
            )

            Spacer(Modifier.height(24.dp))
        }

        item {
            OutlinedButton(
                modifier = Modifier.width(160.dp),
                onClick = onButtonClick
            ) {
                Text(
                    text = "Login",
                    fontSize = typography.bodyLarge.fontSize,
                    fontWeight = typography.bodyLarge.fontWeight
                )
            }
        }
    }
}