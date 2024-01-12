package ui.main.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import api.model.UpdatePasswordRequest
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.settings.SettingsComponent
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import util.Status
import util.Status.Error
import util.Status.Success

@OptIn(ExperimentalResourceApi::class)
@Composable
fun UpdatePassword(
    component: SettingsComponent,
    modifier: Modifier = Modifier,
    onSuccess: (Status) -> Unit
) {
    val scope = rememberCoroutineScope()
    val apiStatus by component.passwordStatus.subscribeAsState()

    val currentPassword by component.currentPassword.subscribeAsState()
    val currentInput by remember(currentPassword) { mutableStateOf(currentPassword) }
    val currentPasswordIsValid by component.currentPasswordIsValid.subscribeAsState()
    var currentPasswordLabel by remember { mutableStateOf<String?>(null) }

    val newPassword by component.newPassword.subscribeAsState()
    val newInput by remember(newPassword) { mutableStateOf(newPassword) }
    val newPasswordIsValid by component.newPasswordIsValid.subscribeAsState()
    var newPasswordLabel by remember { mutableStateOf<String?>(null) }
    var newPasswordVisibility by remember { mutableStateOf(false) }

    val confirmPassword by component.confirmPassword.subscribeAsState()
    val confirmInput by remember(confirmPassword) { mutableStateOf(confirmPassword) }
    val confirmPasswordIsValid by component.currentPasswordIsValid.subscribeAsState()
    var confirmPasswordLabel by remember { mutableStateOf<String?>(null) }
    var confirmPasswordVisibility by remember { mutableStateOf(false) }

    val enabled by derivedStateOf {
        persistentListOf(
            currentPasswordIsValid,
            newPasswordIsValid,
            confirmPasswordIsValid
        ).all { it == Success }
    }

    LaunchedEffect(currentPasswordIsValid) {
        currentPasswordLabel = runCatching { (currentPasswordIsValid as Error).body.toString() }.getOrNull()
    }

    LaunchedEffect(newPasswordIsValid) {
        newPasswordLabel = runCatching { (newPasswordIsValid as Error).body.toString() }.getOrNull()
    }

    LaunchedEffect(confirmPasswordIsValid) {
        confirmPasswordLabel = runCatching { (confirmPasswordIsValid as Error).body.toString() }.getOrNull()
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(9f),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SettingsTextField(
                modifier = Modifier.fillMaxWidth(),
                label = currentPasswordLabel ?: "Current Password",
                input = currentInput,
                onInputChange = component::updateCurrentPassword,
                isError = currentPasswordIsValid is Error,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = { Icon(imageVector = Outlined.Lock, contentDescription = "Current Password") }
            )

            SettingsTextField(
                modifier = Modifier.fillMaxWidth(),
                label = newPasswordLabel ?: "New Password",
                input = newInput,
                onInputChange = component::updateNewPassword,
                isError = newPasswordIsValid is Error,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (newPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(imageVector = Outlined.Lock, contentDescription = "New Password") },
                trailingIcon = {
                    IconToggleButton(
                        checked = newPasswordVisibility,
                        onCheckedChange = { newPasswordVisibility = !newPasswordVisibility },
                        modifier = Modifier.clip(CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(if (newPasswordVisibility) "visibility_off.xml" else "visibility.xml"),
                            contentDescription = "Show New Password?"
                        )
                    }
                }
            )

            SettingsTextField(
                modifier = Modifier.fillMaxWidth(),
                label = confirmPasswordLabel ?: "Confirm New Password",
                input = confirmInput,
                onInputChange = component::updateConfirmPassword,
                isError = confirmPasswordIsValid is Error,
                keyboardType = KeyboardType.Password,
                visualTransformation = if (newPasswordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                leadingIcon = { Icon(imageVector = Outlined.Lock, contentDescription = "Confirm New Password") },
                trailingIcon = {
                    IconToggleButton(
                        checked = confirmPasswordVisibility,
                        onCheckedChange = { confirmPasswordVisibility = !confirmPasswordVisibility },
                        modifier = Modifier.clip(CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(if (confirmPasswordVisibility) "visibility_off.xml" else "visibility.xml"),
                            contentDescription = "Show New Password?"
                        )
                    }
                }
            )
        }

        IconButton(
            modifier = Modifier.size(40.dp),
            onClick = {
                scope.launch {
                    component.validateInputs(component.settings.password.get(), this.coroutineContext)
                    if (enabled) {
                        async {
                            component.updatePassword(
                                update = UpdatePasswordRequest(
                                    oldPassword = currentInput,
                                    newPassword = newInput,
                                    newPasswordConfirm = confirmInput
                                ),
                                context = this@launch.coroutineContext
                            )
                        }.await()
                        onSuccess(apiStatus)
                    }
                }
            },
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = colorScheme.background,
                contentColor = if (!enabled) colorScheme.error else colorScheme.primary,
                disabledContentColor = colorScheme.onBackground
            )
        ) {
            Icon(imageVector = Filled.CheckCircle, contentDescription = "Submit")
        }
    }
}