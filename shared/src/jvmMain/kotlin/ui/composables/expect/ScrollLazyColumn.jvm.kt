package ui.composables.expect

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
actual fun ScrollLazyColumn(
    verticalArrangement: Arrangement.Vertical,
    horizontalAlignment: Alignment.Horizontal,
    paddingValues: PaddingValues?,
    modifier: Modifier,
    items: LazyListScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        val state = rememberLazyListState()
        val padding = paddingValues ?: PaddingValues(vertical = 0.dp)

        LazyColumn(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            modifier = Modifier.fillMaxSize(),
            state = state
        ) { items() }
        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(scrollState = state),
            style = defaultScrollbarStyle().copy(
                unhoverColor = colorScheme.onBackground.copy(alpha = 0.12f),
                hoverColor = colorScheme.onBackground.copy(alpha = 0.5f)
            )
        )
    }
}