package ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import ui.composables.states.RegisterAuthenticationFieldState
import util.Constants.PASSWORDS_NOT_MATCH
import util.Status
import util.Status.Error
import util.Status.Success

@Composable
fun RegisterForm(
    usernameState: RegisterAuthenticationFieldState,
    passwordState: RegisterAuthenticationFieldState,
    confirmPasswordState: RegisterAuthenticationFieldState,
    rememberMe: Boolean,
    modifier: Modifier = Modifier,
    registerStatus: Status = Success,
    usernameStatus: Status = Success,
    passwordStatus: Status = Success,
    isLoading: Boolean = false,
    onCheckChange: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    usernameState.updateIsValid(usernameStatus == Success)
    passwordState.updateIsValid(passwordStatus == Success)
    confirmPasswordState.updateIsValid(
        when {
            passwordStatus == Success -> true
            (passwordStatus as Error).message == PASSWORDS_NOT_MATCH -> false
            else -> true
        }
    )

    ScrollLazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = if (registerStatus is Error) registerStatus.message else "",
                color = if (registerStatus is Error) colorScheme.error else colorScheme.background,
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
                label = if (usernameStatus is Error) usernameStatus.message else "Username"
            )

            Spacer(Modifier.height(50.dp))
        }

        item {
            PasswordField(
                state = passwordState,
                enabled = !isLoading,
                modifier = Modifier.width(300.dp),
                label = if (passwordStatus is Error) passwordStatus.message else "Password"
            )

            Spacer(Modifier.height(50.dp))
        }

        item {
            PasswordField(
                state = confirmPasswordState,
                enabled = !isLoading,
                modifier = Modifier.width(300.dp),
                label = when {
                    passwordStatus == Success -> "Confirm Password"
                    (passwordStatus as Error).message == PASSWORDS_NOT_MATCH -> PASSWORDS_NOT_MATCH
                    else -> "Confirm Password"
                }
            )

            Spacer(Modifier.height(15.dp))
        }

        item {
            RememberMeCheckbox(
                rememberMe = rememberMe,
                enabled = !isLoading,
                onCheckChanged = onCheckChange,
                modifier = Modifier.width(300.dp)
            )

            Spacer(Modifier.height(50.dp))
        }

        item {
            OutlinedButton(modifier = Modifier.width(160.dp), onClick = onClick) {
                Text(
                    text = "Login",
                    fontSize = typography.bodyLarge.fontSize,
                    fontWeight = typography.bodyLarge.fontWeight
                )
            }
        }
    }
}