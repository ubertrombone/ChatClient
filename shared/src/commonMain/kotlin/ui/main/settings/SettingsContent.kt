package ui.main.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import api.model.UpdateUsernameRequest
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.settings.SettingsComponent
import ui.composables.expect.ScrollLazyColumn
import ui.composables.states.rememberUsernameAuthenticationFieldState
import util.SettingsOptions
import util.SettingsOptions.USERNAME
import util.toUsername

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    var selectedSetting by remember { mutableStateOf<SettingsOptions?>(null) }
    val username = rememberUsernameAuthenticationFieldState(component.settings.username.get())
    val usernameValid by username.isValid.subscribeAsState()
    val usernameInput by username.input.subscribeAsState()
//    val coroutineScope = rememberCoroutineScope()
    var label by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = "Settings",
                    fontSize = typography.displayMedium.fontSize,
                    fontWeight = typography.displayMedium.fontWeight
                ) },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Close Settings",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .padding(start = 12.dp, top = 12.dp)
                            .size(40.dp)
                            .padding(5.dp)
                            .clip(CircleShape)
                            .clickable { component.onDismissed() }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        containerColor = colorScheme.background
    ) { padding ->
        ScrollLazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                // TODO: Status
                // TODO: Status validation should be done on client side
            }

            // TODO: Test if changing username works
            // TODO: Move icon out of text field
            item {
                SettingCard(
                    label = "Change Username",
                    selected = selectedSetting == USERNAME,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { selectedSetting = USERNAME.takeUnless { selectedSetting == it } }
                ) {
                    item {
                        UpdateUsername(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                            usernameState = username,
                            label = label
                        ) {
                            component.updateUsernameStatus(username.validateInput())
                            if (usernameValid && usernameInput != component.settings.username.get())
                                component.updateUsername(UpdateUsernameRequest(usernameInput.toUsername()), it)
                            label = component.getUsernameAsResponse()
                        }
                    }
                }
            }

            item {
                // TODO: Button to change password. 2 options:
                //  1. Button navs to another screen with: current password, new password, confirm new password fields
                //  2. This items transforms into the 3 text fields plus a submission button. -- would need to learn
                //      how to animate the transition
            }

            item {
                // TODO: Check box with info about caching chats on the server.
            }

            item {
                // TODO: Big red delete button
                // TODO: Will require a popup confirmation
            }
        }
    }
}