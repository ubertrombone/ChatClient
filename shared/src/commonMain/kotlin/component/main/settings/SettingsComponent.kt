package component.main.settings

import api.ApplicationApi
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow
import settings.SettingsRepository

interface SettingsComponent {
    val settings: SettingsRepository
    val server: ApplicationApi
    val onDismissed: () -> Unit
    val logout: () -> Unit

    val status: StateFlow<String?>
    val statusLoading: Value<Boolean>
    val cache: StateFlow<Boolean>
    val cacheLoading: Value<Boolean>

    suspend fun getStatus()
    suspend fun updateStatus()
    suspend fun getCache()
    suspend fun updateCache()
    suspend fun updateUsername()
    suspend fun updatePassword()
    suspend fun deleteAccount()
}