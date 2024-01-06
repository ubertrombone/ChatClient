package ui.main.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.composables.expect.ScrollLazyColumn
import util.tweenSpec

@Composable
fun SettingCard(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onSelected: () -> Unit,
    content: LazyListScope.() -> Unit
) {
    val angle = remember { Animatable(0f) }
    val height = remember { Animatable(100.dp.value) }

    LaunchedEffect(selected) {
        launch {
            angle.animateTo(
                targetValue = if (selected) 180f else 0f,
                animationSpec = tweenSpec()
            )
            height.animateTo(
                targetValue = if (selected) 400.dp.value else 100.dp.value,
                animationSpec = tweenSpec()
            )
        }
    }

    Column(
        modifier = modifier
            .background(colorScheme.background)
            .height(height.value.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onSelected() }
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                fontSize = typography.bodyMedium.fontSize,
                softWrap = false,
                color = colorScheme.primary
            )

            Icon(
                modifier = Modifier.rotate(angle.value),
                imageVector = Default.KeyboardArrowDown,
                contentDescription = "Select $label setting",
                tint = colorScheme.primary
            )
        }

        AnimatedVisibility(
            visible = selected,
            enter = expandVertically(animationSpec = tweenSpec()) + fadeIn(animationSpec = tweenSpec()),
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        ) {
            ScrollLazyColumn(modifier = Modifier.fillMaxWidth(), items = content)
        }
    }
}