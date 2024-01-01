package ui.main.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
        itemsIndexed(items = list.toImmutableList()) { index, item ->
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FriendCard(
                    modifier = Modifier.fillMaxSize(),
                    friendInfo = item,
                    onClick = friendSelected
                )

                if (index != list.indices.last) Divider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )
            }

        }
    }
}