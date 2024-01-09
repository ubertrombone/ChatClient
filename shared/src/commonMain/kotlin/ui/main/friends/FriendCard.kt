package ui.main.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.MarqueeAnimationMode.Companion.Immediately
import androidx.compose.foundation.MarqueeAnimationMode.Companion.WhileFocused
import androidx.compose.foundation.MarqueeSpacing
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import api.model.FriendInfo
import util.ago

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FriendCard(
    friendInfo: FriendInfo,
    modifier: Modifier = Modifier,
    onClick: (FriendInfo) -> Unit
) {
    var focused by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        focused = when (event.type) {
                            PointerEventType.Enter -> true
                            PointerEventType.Exit -> false
                            PointerEventType.Press -> true
                            PointerEventType.Release -> false
                            else -> continue
                        }
                    }
                }
            },
        onClick = { onClick(friendInfo) },
        shape = RectangleShape,
        colors = CardDefaults.cardColors(containerColor = colorScheme.background, contentColor = colorScheme.primary)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.SpaceBetween) {
                Text(text = friendInfo.username.name, fontSize = typography.bodyLarge.fontSize)
                if (!friendInfo.isOnline) {
                    Text(
                        text = friendInfo.lastOnline!!.ago(),
                        fontSize = typography.bodyMedium.fontSize,
                        fontWeight = FontWeight.Thin
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (friendInfo.isOnline) Color.Green else Color.Gray)
            )
        }

        Text(
            text = friendInfo.status ?: "",
            fontSize = typography.bodyMedium.fontSize,
            softWrap = false,
            maxLines = 1,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 4.dp)
                .basicMarquee(
                    iterations = Int.MAX_VALUE,
                    initialDelayMillis = 500,
                    delayMillis = 1000,
                    animationMode = if (focused) Immediately else WhileFocused,
                    spacing = MarqueeSpacing(100.dp),
                    velocity = 60.dp
                )
        )
    }
}