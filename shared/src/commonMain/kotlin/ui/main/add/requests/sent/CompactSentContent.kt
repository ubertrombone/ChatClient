package ui.main.add.requests.sent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import component.main.add.requests.sent.SentRequestsComponent
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ui.icons.ReceivedRequestsIcon
import ui.icons.SearchIcon

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun CompactSentContent(component: SentRequestsComponent, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(bottom = 12.dp),
                title = {},
                navigationIcon = { SearchIcon { component.dismiss() } },
                actions = {
                    ReceivedRequestsIcon(
                        modifier = Modifier
                            .padding(top = 12.dp, end = 12.dp)
                            .padding(5.dp)
                    ) { component.navBack() }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        containerColor = colorScheme.background
    ) { SentContent(component = component, modifier = Modifier.fillMaxSize().padding(it)) }
}