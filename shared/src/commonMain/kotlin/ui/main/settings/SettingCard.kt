package ui.main.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.composables.expect.ScrollLazyColumn

@Composable
fun SettingCard(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelected: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    Column(modifier = modifier.background(colorScheme.background), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .clickable { onSelected() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = typography.labelLarge.fontSize,
                softWrap = false,
                color = colorScheme.primary
            )

            Icon(
                imageVector = if (selected) Default.KeyboardArrowUp else Default.KeyboardArrowDown,
                contentDescription = "Select $label setting",
                tint = colorScheme.primary
            )
        }

        if (selected) ScrollLazyColumn(modifier = Modifier.fillMaxWidth(), items = content)
    }
}