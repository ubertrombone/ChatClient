package ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    usernameState.validateInput(loginStatus, type = USERNAME)
    passwordState.validateInput(loginStatus, type = PASSWORD)

    ScrollLazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = when {
                    loginStatus == Success -> ""
                    (loginStatus as Error).message == NO_PASSWORD_PROVIDED -> ""
                    loginStatus.message == INVALID_USERNAME -> ""
                    else -> loginStatus.message
                },
                color = when {
                    loginStatus == Success -> colorScheme.background
                    (loginStatus as Error).message == NO_PASSWORD_PROVIDED -> colorScheme.background
                    loginStatus.message == INVALID_USERNAME -> colorScheme.background
                    else -> colorScheme.error
                },
                fontSize = typography.bodyLarge.fontSize,
                fontWeight = typography.bodyLarge.fontWeight
            )

            Spacer(Modifier.height(20.dp))
        }

        item {
            UsernameField(
                state = usernameState,
                enabled = !isLoading,
                modifier = Modifier.width(300.dp),
                label = when {
                    loginStatus == Success -> "Username"
                    (loginStatus as Error).message == INVALID_USERNAME -> loginStatus.message
                    else -> "Username"
                }
            )

            Spacer(Modifier.height(50.dp))
        }

        item {
            PasswordField(
                state = passwordState,
                enabled = !isLoading,
                modifier = Modifier.width(300.dp),
                label = when {
                    loginStatus == Success -> "Password"
                    (loginStatus as Error).message == NO_PASSWORD_PROVIDED -> loginStatus.message
                    else -> "Password"
                }
            )

            Spacer(Modifier.height(15.dp))
        }

        item {
            RememberMeCheckbox(
                rememberMe = rememberMe,
                enabled = !isLoading,
                onCheckChanged = onCheckChange
            )

            Spacer(Modifier.height(50.dp))
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