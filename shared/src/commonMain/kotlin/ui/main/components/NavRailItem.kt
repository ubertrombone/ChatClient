package ui.main.components

import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun NavRailItem(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    NavigationRailItem(
        modifier = modifier,
        label = { Text(text = label, softWrap = true, textAlign = TextAlign.Center) },
        icon = icon,
        selected = selected,
        onClick = onClick,
        alwaysShowLabel = false,
        colors = NavigationRailItemDefaults.colors(
            unselectedIconColor = colorScheme.onPrimary,
            unselectedTextColor = colorScheme.onPrimary,
            selectedIconColor = colorScheme.onPrimaryContainer,
            selectedTextColor = colorScheme.primaryContainer,
            indicatorColor = colorScheme.primaryContainer
        )
    )
}