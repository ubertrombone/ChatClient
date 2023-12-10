package component.root

import com.arkivanov.decompose.ComponentContext
import settings.SettingsRepository

class DefaultRootComponent(
    componentContext: ComponentContext,
    private val settingsRepository: SettingsRepository
) : RootComponent, ComponentContext by componentContext {
    override val root = "root"
}