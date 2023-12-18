package component.main.settings

import api.ApplicationApi
import settings.SettingsRepository

interface SettingsComponent {
    val settings: SettingsRepository
    val server: ApplicationApi
}