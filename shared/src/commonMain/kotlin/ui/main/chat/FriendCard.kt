package ui.main.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import api.model.FriendInfo
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ui.main.CompactScreen
import ui.main.ExpandedScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendCard(
    friendInfo: FriendInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        enabled = friendInfo.isOnline,
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.background,
            contentColor = colorScheme.primary,
            disabledContainerColor = colorScheme.background,
            disabledContentColor = colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier.padding(start = 4.dp, end = 4.dp, top = 4.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = friendInfo.username.name, fontSize = typography.bodyLarge.fontSize)
                if (!friendInfo.isOnline) Text(
                    text = friendInfo.lastOnline!!.toLocalDateTime(timeZone = TimeZone.currentSystemDefault()).toString(),
                    fontSize = typography.bodyMedium.fontSize,
                    fontWeight = FontWeight.Thin
                )
            }

            Spacer(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (friendInfo.isOnline) Color.Green else Color.Gray)
            )
        }

        // TODO: 4. If not null, user's status. Should be ellipsized to not cover "last online" when visible, and should marquis when card is hovered
    }
}