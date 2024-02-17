package ui.main.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import chatclient.shared.generated.resources.Res
import chatclient.shared.generated.resources.add_friend
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AddFriendTab(size: Int) =
    if (size > 0) BadgedBox(badge = { Badge { Text(text = "$size") } }) {
        Icon(painter = painterResource(Res.drawable.add_friend), contentDescription = "Friend requests")
    } else Icon(painter = painterResource(Res.drawable.add_friend), contentDescription = "Friend requests")