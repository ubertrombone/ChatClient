package ui.login

import androidx.compose.foundation.layout.*
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
import ui.composables.states.AuthenticationFieldState

@Composable
fun LoginForm(
    usernameState: AuthenticationFieldState,
    passwordState: AuthenticationFieldState,
    rememberMe: Boolean,
    modifier: Modifier = Modifier,
    onCheckChange: (Boolean) -> Unit,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UsernameField(modifier = Modifier.width(300.dp), state = usernameState)

        Spacer(Modifier.height(50.dp))

        PasswordField(modifier = Modifier.width(300.dp), state = passwordState)

        Spacer(Modifier.height(15.dp))

        RememberMeCheckbox(rememberMe = rememberMe, onCheckChanged = onCheckChange)

        Spacer(Modifier.height(50.dp))

        OutlinedButton(modifier = Modifier.width(160.dp), onClick = onButtonClick) {
            Text(
                text = "Login",
                fontSize = typography.bodyLarge.fontSize,
                fontWeight = typography.bodyLarge.fontWeight
            )
        }
    }
}