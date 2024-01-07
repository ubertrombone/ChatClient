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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import component.main.settings.SettingsComponent
import ui.composables.expect.ScrollLazyColumn
import ui.composables.states.rememberStatusAuthenticationFieldState
import ui.composables.states.rememberUsernameAuthenticationFieldState
import util.SettingsOptions
import util.SettingsOptions.STATUS
import util.SettingsOptions.USERNAME

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    var selectedSetting by remember { mutableStateOf<SettingsOptions?>(null) }
    val status = rememberStatusAuthenticationFieldState(component.settings.status.get())
    val username = rememberUsernameAuthenticationFieldState(component.settings.username.get())

    LaunchedEffect(Unit) { component.getStatus(this.coroutineContext) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(bottom = 12.dp),
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
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }

            item {
                SettingCard(
                    label = "Update Status",
                    selected = selectedSetting == STATUS,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { selectedSetting = STATUS.takeUnless { selectedSetting == it } }
                ) {
                    UpdateStatus(
                        modifier = Modifier.fillMaxWidth(),
                        component = component,
                        statusState = status,
                        currentStatus = component.settings.status.get()
                    ) { selectedSetting = null }
                }
            }

            item {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }

            item {
                SettingCard(
                    label = "Change Username",
                    selected = selectedSetting == USERNAME,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { selectedSetting = USERNAME.takeUnless { selectedSetting == it } }
                ) {
                    UpdateUsername(
                        modifier = Modifier.fillMaxWidth(),
                        component = component,
                        usernameState = username,
                        currentUsername = component.settings.username.get()
                    )
                }
            }

            item {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )
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