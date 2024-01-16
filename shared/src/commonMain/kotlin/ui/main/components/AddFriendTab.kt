package ui.main.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun AddFriendTab(size: Int) =
    if (size > 0) BadgedBox(badge = { Badge { Text(text = "$size") } }) {
        Icon(painter = painterResource("add_friend.xml"), contentDescription = "Friend requests")
    } else Icon(painter = painterResource("add_friend.xml"), contentDescription = "Friend requests")