package ui.main.add.requests

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import ui.composables.expect.ScrollLazyColumn
import util.Status
import util.Status.Error
import util.Status.Success

@Composable
fun <T> RequestSwitch(
    label: String,
    results: ImmutableList<T>,
    status: Status,
    loading: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    Box(modifier) {
        when {
            loading -> CircularProgressIndicator(
                modifier = Modifier.size(120.dp).align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            status is Error -> Text(text = status.body.toString(), modifier = Modifier.align(Alignment.Center))
            status == Success ->
                if (results.isEmpty()) Text(text = label, modifier = Modifier.align(Alignment.Center))
                else ScrollLazyColumn(modifier = Modifier.fillMaxWidth()) { items(results) { content(it) } }
        }
    }
}