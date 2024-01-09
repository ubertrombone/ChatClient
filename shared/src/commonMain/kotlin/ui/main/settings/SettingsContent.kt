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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.main.settings.SettingsComponent
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import ui.composables.NavBackButton
import ui.composables.expect.ScrollLazyColumn
import ui.composables.states.rememberPasswordAuthenticationFieldState
import ui.composables.states.rememberStatusAuthenticationFieldState
import ui.composables.states.rememberUsernameAuthenticationFieldState
import util.SettingsOptions
import util.SettingsOptions.*
import util.Status
import util.Status.Success

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SettingsContent(component: SettingsComponent, modifier: Modifier = Modifier) {
    val windowSizeClass = calculateWindowSizeClass()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selectedSetting by remember { mutableStateOf<SettingsOptions?>(null) }
    val status = rememberStatusAuthenticationFieldState(component.settings.status.get())
    val username = rememberUsernameAuthenticationFieldState(component.settings.username.get())
    val password = rememberPasswordAuthenticationFieldState()
    val deleteStatus by component.deleteAccountStatus.subscribeAsState()

    LaunchedEffect(Unit) {
        component.getStatus(this.coroutineContext)
        component.getCache(this.coroutineContext)
    }

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
                    containerColor = colorScheme.primaryContainer.copy(alpha = .7f),
                    contentColor = colorScheme.onPrimaryContainer,
                    dismissActionContentColor = colorScheme.onPrimaryContainer
                )
            }
        },
        containerColor = colorScheme.background
    ) { padding ->
        // TODO: Delete Account button needs confirmation dialog
        ScrollLazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item { DividerHelper() }

            item {
                SettingCard(
                    label = "Update Status",
                    selected = selectedSetting == STATUS,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { selectedSetting = STATUS.takeUnless { selectedSetting == it } }
                ) {
                    UpdateStatus(
                        modifier = Modifier.fillMaxWidth(),
                        component = component,
                        statusState = status
                    ) {
                        if (it == Success) scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Successfully updated status!",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }

            item { DividerHelper() }

            item {
                SettingCard(
                    label = "Change Username",
                    selected = selectedSetting == USERNAME,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { selectedSetting = USERNAME.takeUnless { selectedSetting == it } }
                ) {
                    UpdateUsername(
                        modifier = Modifier.fillMaxWidth(),
                        component = component,
                        usernameState = username
                    ) {
                        if (it == Success) scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Successfully updated username!",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }

            item { DividerHelper() }

            item {
                SettingCard(
                    label = "Change Password",
                    selected = selectedSetting == PASSWORD,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { selectedSetting = PASSWORD.takeUnless { selectedSetting == it } }
                ) {
                    UpdatePassword(
                        modifier = Modifier.fillMaxWidth(),
                        component = component,
                        states = password
                    ) {
                        if (it == Success) {
                            password.clear()
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Successfully updated password!",
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    }
                }
            }

            item { DividerHelper() }

            item {
                SettingCard(
                    label = "Cache Messages",
                    selected = selectedSetting == CACHE,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { selectedSetting = CACHE.takeUnless { selectedSetting == it } }
                ) {
                    UpdateCache(
                        initCache = component.settings.cache.get().toBooleanStrict(),
                        component = component,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Updated Cache: ${it.toString().replaceFirstChar { it.uppercase() }}",
                                withDismissAction = true,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }

            item { DividerHelper() }

            item {
                SettingCard(
                    label = "Delete Account",
                    selected = selectedSetting == DELETE,
                    modifier = Modifier.fillMaxWidth(),
                    onSelected = { selectedSetting = DELETE.takeUnless { selectedSetting == it } }
                ) {
                    OutlinedButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp),
                        onClick = {
                            scope.launch {
                                component.deleteAccount(true, this.coroutineContext)
                                snackbarHostState.showSnackbar(
                                    message = if (deleteStatus is Status.Error)
                                        ((deleteStatus as Status.Error).body as HttpResponse).bodyAsText() else "Account Deleted!",
                                    withDismissAction = true,
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = colorScheme.onErrorContainer,
                            contentColor = colorScheme.onError
                        )
                    ) {
                        Text(
                            text = "Delete Account",
                            fontSize = typography.bodyLarge.fontSize,
                            fontWeight = typography.bodyLarge.fontWeight
                        )
                    }
                }
            }

            item { DividerHelper() }
        }
    }
}

@Composable
fun DividerHelper(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = Color.Gray
) {
    Divider(
        modifier = modifier,
        thickness = thickness,
        color = color
    )
}