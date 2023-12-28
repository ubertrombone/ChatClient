package component.main.settings

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import settings.SettingsRepository

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val settings: SettingsRepository,
    override val onDismissed: () -> Unit
) : SettingsComponent, ComponentContext by componentContext