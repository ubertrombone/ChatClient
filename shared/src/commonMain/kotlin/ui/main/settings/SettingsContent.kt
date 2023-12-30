package ui.main.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import component.main.settings.SettingsComponent
import ui.composables.expect.ScrollLazyColumn

@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = colorScheme.tertiaryContainer.copy(alpha = .8f),
        contentColor = colorScheme.primary,
        tonalElevation = 24.dp,
    ) {
        ScrollLazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                Text(text = "SETTINGS VIEW", fontSize = typography.displayLarge.fontSize)
            }

            item {
                // TODO: Button to change username.
                //   When clicked it turns into row that contains text field and check/submit button
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
                // TODO: Logout button
            }

            item {
                // TODO: Big red delete button
                // TODO: Will require a popup confirmation
            }
        }
    }
}