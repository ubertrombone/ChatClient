package ui.main.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass.Companion.Compact
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Companion.Expanded
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.settings.SettingsComponent
import component.main.settings.util.SettingsOptions.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import ui.composables.Divider
import ui.composables.expect.ScrollLazyColumn
import ui.composables.snackbarHelper
import ui.icons.NavBackButton
import util.BottomBarSystemNavColor
import util.SoftInputMode
import util.Status.Error
import util.Status.Success

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    val warningSlot by component.deleteDialogSlot.subscribeAsState()
    val windowSizeClass = calculateWindowSizeClass()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val selectedSetting by component.settingsOptions.collectAsState()
    val deleteStatus by component.deleteAccountStatus.subscribeAsState()

    LaunchedEffect(Unit) {
        component.getStatus(this.coroutineContext)
        component.getCache(this.coroutineContext)
    }

    LaunchedEffect(deleteStatus) {
        if (deleteStatus is Error)
            snackbarHostState.snackbarHelper(message = ((deleteStatus as Error).body as HttpResponse).bodyAsText())
        if (deleteStatus == Success) snackbarHostState.snackbarHelper(message = "Account Deleted!")
    }

    BottomBarSystemNavColor(colorScheme.background)
    SoftInputMode(true)

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier.padding(bottom = 12.dp),
                title = { Text(
                    text = "Settings",
                    fontSize = typography.displayMedium.fontSize,
                    fontWeight = typography.displayMedium.fontWeight
                ) },
                navigationIcon = {
                    if (windowSizeClass.widthSizeClass != Expanded || windowSizeClass.heightSizeClass == Compact)
                        NavBackButton { component.onDismissed() }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.primary
                )
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = colorScheme.primaryContainer.copy(alpha = .7f), // TODO: Something wrong here in light mode
                    contentColor = colorScheme.onPrimaryContainer,
                    dismissActionContentColor = colorScheme.onPrimaryContainer
                )
            }
        },
        containerColor = colorScheme.background
    ) { padding ->
        ScrollLazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item { Divider() }

            item {
                SettingCard(
                    label = "Update Status",
                    selected = selectedSetting == STATUS,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { component.updateSettingsOptions(STATUS.takeUnless { selectedSetting == it }) }
                ) {
                    UpdateStatus(modifier = Modifier.fillMaxWidth(), component = component) {
                        if (it == Success) scope.launch {
                            snackbarHostState.snackbarHelper(message = "Successfully updated status!")
                        }
                    }
                }
            }

            item { Divider() }

            item {
                SettingCard(
                    label = "Change Username",
                    selected = selectedSetting == USERNAME,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { component.updateSettingsOptions(USERNAME.takeUnless { selectedSetting == it }) }
                ) {
                    UpdateUsername(modifier = Modifier.fillMaxWidth(), component = component) {
                        if (it == Success) scope.launch {
                            snackbarHostState.snackbarHelper(message = "Successfully updated username!")
                        }
                    }
                }
            }

            item { Divider() }

            item {
                SettingCard(
                    label = "Change Password",
                    selected = selectedSetting == PASSWORD,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { component.updateSettingsOptions(PASSWORD.takeUnless { selectedSetting == it }) }
                ) {
                    UpdatePassword(modifier = Modifier.fillMaxWidth(), component = component) {
                        if (it == Success) {
                            component.clearPasswords()
                            scope.launch {
                                snackbarHostState.snackbarHelper(message = "Successfully updated password!")
                            }
                        }
                    }
                }
            }

            item { Divider() }

            item {
                SettingCard(
                    label = "Cache Messages",
                    selected = selectedSetting == CACHE,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { component.updateSettingsOptions(CACHE.takeUnless { selectedSetting == it }) }
                ) {
                    UpdateCache(
                        initCache = component.settings.cache.get().toBooleanStrict(),
                        component = component,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        scope.launch {
                            snackbarHostState.snackbarHelper(
                                message = "Updated Cache: ${it.toString().replaceFirstChar { it.uppercase() }}"
                            )
                        }
                    }
                }
            }

            item { Divider() }

            item {
                SettingCard(
                    label = "Delete Account",
                    selected = selectedSetting == DELETE,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { component.updateSettingsOptions(DELETE.takeUnless { selectedSetting == it }) }
                ) {
                    DeleteAccount(
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp),
                        onClick = component::showDeleteAccountWarning
                    )
                }
            }

            item { Divider() }
        }

        warningSlot.child?.instance?.also { WarningContent(component = it) }
    }
}