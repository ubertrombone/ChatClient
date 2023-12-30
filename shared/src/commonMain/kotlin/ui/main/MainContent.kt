package ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import component.main.MainComponent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainContent(component: MainComponent, modifier: Modifier = Modifier) {
    val windowSizeClass = calculateWindowSizeClass()

    // TODO:
    //  Components:
    //      3. friend list in lazy column
    //          - immutable list returned by API
    //          - clicking friend card navs to chat screen

    if (windowSizeClass.widthSizeClass == Expanded && windowSizeClass.heightSizeClass != Compact)
        Box(modifier, contentAlignment = Alignment.Center) {
            Text(
                text = "EXPANDED VIEW",
                fontSize = typography.displayLarge.fontSize
            )
        }
    else CompactScreen(component, modifier)
}