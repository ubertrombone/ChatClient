package ui.composables

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
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
            unselectedIconColor = MaterialTheme.colorScheme.onPrimary,
            unselectedTextColor = MaterialTheme.colorScheme.onPrimary,
            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            selectedTextColor = MaterialTheme.colorScheme.primaryContainer,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}