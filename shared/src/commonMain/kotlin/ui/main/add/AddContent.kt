package ui.main.add

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import component.main.add.AddComponent
import util.SoftInputMode

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun AddContent(component: AddComponent, modifier: Modifier = Modifier) {
    val windowSizeClass = calculateWindowSizeClass()
    SoftInputMode(false)

    if (windowSizeClass.widthSizeClass == Expanded && windowSizeClass.heightSizeClass != Compact)
        ExpandedAddContent(component, modifier)
    else CompactAddContent(component, modifier)
}