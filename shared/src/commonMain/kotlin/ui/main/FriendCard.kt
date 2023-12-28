package ui.main

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import api.model.FriendInfo

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun FriendCard(
    friendInfo: FriendInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { /* TODO: show status as marquis */ }
            .onPointerEvent(PointerEventType.Exit) { /* TODO: stop showing marquis */ },
        onClick = onClick,
        enabled = friendInfo.isOnline,
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.background,
            contentColor = colorScheme.primary,
            disabledContainerColor = colorScheme.background,
            disabledContentColor = colorScheme.primary
        )
    ) {
        // TODO: 1. Username top left, slightly larger font,
        // TODO: 2. Green or Gray indicator in top right to show if user is online or offline,
        // TODO: 3. If offline, date when last online in bottom right,
        // TODO: 4. If not null, user's status. Should be ellipsized to not cover "last online" when visible, and should marquis when card is hovered
    }
}