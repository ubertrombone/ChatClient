package ui.main.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowScope.NavBarItem(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    NavigationBarItem(
        modifier = modifier,
        label = { Text(text = label, softWrap = true) },
        icon = icon,
        selected = selected,
        onClick = onClick,
        alwaysShowLabel = false,
        colors = NavigationBarItemDefaults.colors(
            unselectedIconColor = colorScheme.onPrimary,
            unselectedTextColor = colorScheme.onPrimary,
            selectedIconColor = colorScheme.onPrimaryContainer,
            selectedTextColor = colorScheme.primaryContainer,
            indicatorColor = colorScheme.primaryContainer
        )
    )
}