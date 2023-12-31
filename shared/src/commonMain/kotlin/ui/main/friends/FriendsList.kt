package ui.main.friends

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import api.model.FriendInfo
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import ui.composables.expect.ScrollLazyColumn

@Composable
fun FriendsList(
    list: ImmutableSet<FriendInfo>,
    modifier: Modifier = Modifier,
    friendSelected: (FriendInfo) -> Unit
) {
    ScrollLazyColumn(modifier = modifier) {
        items(items = list.toImmutableList()) {
            FriendCard(
                friendInfo = it,
                onClick = friendSelected
            )
        }
    }
}