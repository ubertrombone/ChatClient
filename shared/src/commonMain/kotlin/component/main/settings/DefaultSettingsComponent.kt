package component.main.settings

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import settings.SettingsRepository

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val settings: SettingsRepository,
    override val onDismissed: () -> Unit,
    override val logout: () -> Unit
) : SettingsComponent, ComponentContext by componentContext {
    val scope = CoroutineScope(Dispatchers.IO)

    private val _status = MutableStateFlow<String?>(null)
    override val status: StateFlow<String?> = _status

    private val _statusLoading = MutableValue(true)
    override val statusLoading: Value<Boolean> = _statusLoading

    private val _cache = MutableStateFlow(false)
    override val cache: StateFlow<Boolean> = _cache

    private val _cacheLoading = MutableValue(false)
    override val cacheLoading: Value<Boolean> = _cacheLoading

    override suspend fun getStatus() {
        callWrapper(
            isLoading = _statusLoading,
            operation = { server.getStatus() },
            onSuccess = { status ->
                _status.update { status }
            },
            onError = {}
        )
    }

    override suspend fun updateStatus() {
        TODO("Not yet implemented")
    }

    override suspend fun getCache() {
        TODO("Not yet implemented")
    }

    override suspend fun updateCache() {
        TODO("Not yet implemented")
    }

    override suspend fun updateUsername() {
        TODO("Not yet implemented")
    }

    override suspend fun updatePassword() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount() {
        TODO("Not yet implemented")
    }
}