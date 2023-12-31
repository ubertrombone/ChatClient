package ui.main.friends

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import component.main.friends.FriendsComponent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun FriendsContent(component: FriendsComponent, modifier: Modifier = Modifier) {
    val windowSizeClass = calculateWindowSizeClass()

    if (windowSizeClass.widthSizeClass == Expanded && windowSizeClass.heightSizeClass != Compact)
        ExpandedFriendsContent(component, modifier)
    else CompactFriendsContent(component, modifier)
}