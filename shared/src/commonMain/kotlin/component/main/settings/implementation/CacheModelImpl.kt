package component.main.settings.implementation

import api.ApplicationApi
import api.callWrapper
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import component.main.settings.interfaces.LocalModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import settings.SettingsRepository
import util.Status
import util.Status.Error
import kotlin.coroutines.CoroutineContext

class CacheModelImpl(
    initialLoading: Boolean,
    initialStatus: Status,
    private val settings: SettingsRepository,
    private val server: ApplicationApi
) : LocalModel, InstanceKeeper.Instance {
    override val scope = CoroutineScope(Dispatchers.Main)
    override val loadingState = MutableValue(initialLoading)
    override val status = MutableValue(initialStatus)

    override suspend fun get(context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.getCache() },
            onSuccess = { cache ->
                with(settings.cache.get().toBooleanStrict()) {
                    if ((cache == true) != this) scope.launch { update(this@with, this.coroutineContext) }
                }
            },
            onError = { status.update { _ -> Error(it) } }
        )
    }

    override suspend fun <T> update(value: T, context: CoroutineContext) = withContext(context) {
        callWrapper(
            isLoading = loadingState,
            operation = { server.update(cache = value as Boolean) },
            onSuccess = { status.update { _ -> it } },
            onError = { status.update { _ -> Error(it) } }
        )
    }
}