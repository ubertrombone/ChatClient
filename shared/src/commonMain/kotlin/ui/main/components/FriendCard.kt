package ui.main.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import api.model.FriendInfo
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FriendCard(
    friendInfo: FriendInfo,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = colorScheme.background, contentColor = colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 4.dp, end = 4.dp, top = 4.dp),
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

        friendInfo.status?.let {
            Text(
                text = it,
                fontSize = typography.bodyMedium.fontSize,
                softWrap = false,
                maxLines = 1,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(animationMode = MarqueeAnimationMode.WhileFocused)
            )
        }
    }
}