package ui.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun NavBackButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Icon(
        imageVector = Outlined.ArrowBack,
        contentDescription = "Return to previous page",
        tint = colorScheme.primary,
        modifier = modifier
            .padding(start = 12.dp, top = 12.dp)
            .size(40.dp)
            .padding(5.dp)
            .clip(CircleShape)
            .clickable { onClick() }
    )
}