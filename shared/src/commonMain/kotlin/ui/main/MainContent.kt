package ui.main

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import component.main.MainComponent

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MainContent(component: MainComponent, modifier: Modifier = Modifier) {
    val windowSizeClass = calculateWindowSizeClass()

    if (windowSizeClass.widthSizeClass == Expanded && windowSizeClass.heightSizeClass != Compact)
        ExpandedScreen(component, modifier)
    else CompactScreen(component, modifier)
}