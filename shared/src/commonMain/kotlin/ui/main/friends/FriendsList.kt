package ui.main.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import api.model.FriendInfo
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.toImmutableList
import ui.composables.Divider
import ui.composables.expect.ScrollLazyColumn

@Composable
fun FriendsList(
    list: ImmutableSet<FriendInfo>,
    modifier: Modifier = Modifier,
    friendSelected: (FriendInfo) -> Unit
) {
    ScrollLazyColumn(modifier = modifier) {
        itemsIndexed(items = list.toImmutableList()) { index, item ->
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (index == 0) Divider()

                FriendCard(
                    modifier = Modifier.fillMaxSize(),
                    friendInfo = item,
                    onClick = friendSelected
                )

                Divider()
            }
        }
    }
}