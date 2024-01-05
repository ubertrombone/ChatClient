package component.main.settings

import api.ApplicationApi
import api.model.UpdatePasswordRequest
import api.model.UpdateUsernameRequest
import com.arkivanov.decompose.value.Value
import settings.SettingsRepository
import util.Status
import kotlin.coroutines.CoroutineContext

interface SettingsComponent {
    val settings: SettingsRepository
    val server: ApplicationApi
    val onDismissed: () -> Unit
    val logout: () -> Unit

    val statusLoading: Value<Boolean>
    val updateStatusStatus: Value<Status>
    suspend fun getStatus(context: CoroutineContext)
    suspend fun updateStatus(status: String, context: CoroutineContext)

    val cacheLoading: Value<Boolean>
    val updateCacheStatus: Value<Status>
    suspend fun getCache(context: CoroutineContext)
    suspend fun updateCache(cache: Boolean, context: CoroutineContext)

    val usernameLoading: Value<Boolean>
    val usernameStatus: Value<Status>
    suspend fun updateUsername(update: UpdateUsernameRequest, context: CoroutineContext) // requires internet, so should only update this value if response is OK

    val passwordLoading: Value<Boolean>
    val passwordStatus: Value<Status>
    suspend fun updatePassword(update: UpdatePasswordRequest, context: CoroutineContext) // requires internet, so should only update this value if response is OK

    val deleteAccountLoading: Value<Boolean>
    val deleteAccountStatus: Value<Status>
    suspend fun deleteAccount(delete: Boolean, context: CoroutineContext)
}