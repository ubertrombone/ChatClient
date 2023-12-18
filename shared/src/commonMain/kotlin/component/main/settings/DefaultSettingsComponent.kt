package component.main.settings

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import settings.SettingsRepository

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val settings: SettingsRepository
) : SettingsComponent, ComponentContext by componentContext