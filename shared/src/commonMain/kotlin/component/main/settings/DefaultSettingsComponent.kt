package component.main.settings

import api.ApplicationApi
import api.model.UpdatePasswordRequest
import api.model.UpdateUsernameRequest
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.settings.implementation.*
import component.main.settings.warning.DefaultDeleteAccountComponent
import component.main.settings.warning.DeleteAccountComponent
import io.ktor.client.statement.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import settings.SettingsRepository
import util.Constants.USERNAME_EXISTS
import util.Status
import util.Status.Error
import util.Status.Success
import kotlin.coroutines.CoroutineContext

class DefaultSettingsComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val settings: SettingsRepository,
    override val onDismissed: () -> Unit,
    override val logout: () -> Unit
) : SettingsComponent, ComponentContext by componentContext {
    private val deleteAccountNavigation = SlotNavigation<DeleteAccountWarningConfig>()
    private val _deleteDialogSlot = childSlot(
        source = deleteAccountNavigation,
        serializer = DeleteAccountWarningConfig.serializer(),
        handleBackButton = true
    ) { _, childComponentContext ->
        DefaultDeleteAccountComponent(
            childComponentContext,
            deleteAccount = {
                deleteAccount(true, it)
                dismissDeleteAccountWarning()
            },
            dismiss = ::dismissDeleteAccountWarning
        )
    }
    override val deleteDialogSlot: Value<ChildSlot<*, DeleteAccountComponent>> = _deleteDialogSlot
    override fun showDeleteAccountWarning() = deleteAccountNavigation.activate(DeleteAccountWarningConfig)
    override fun dismissDeleteAccountWarning() = deleteAccountNavigation.dismiss()

    private val _statusUpdateStates = instanceKeeper.getOrCreate { StatusModelImpl(settings = settings, server = server) }
    override val statusLoading: Value<Boolean> = _statusUpdateStates.loadingState
    override val updateStatusStatus: Value<Status> = _statusUpdateStates.status
    override suspend fun getStatus(context: CoroutineContext) = _statusUpdateStates.get(context)
    override suspend fun updateStatus(status: String, context: CoroutineContext) = withContext(context) {
        settings.status.set(status)
        _statusUpdateStates.update(status, context)
    }

    private val _cacheUpdateStates = instanceKeeper.getOrCreate { CacheModelImpl(settings, server) }
    override val cacheLoading: Value<Boolean> = _cacheUpdateStates.loadingState
    override val updateCacheStatus: Value<Status> = _cacheUpdateStates.status
    override suspend fun getCache(context: CoroutineContext) = _cacheUpdateStates.get(context)
    override suspend fun updateCache(cache: Boolean, context: CoroutineContext) {
        settings.cache.set(cache)
        _cacheUpdateStates.update(cache, context)
    }

    private val _updateUsernameStates = instanceKeeper.getOrCreate { UsernameModelImpl(server, settings) }
    override val usernameLoading: Value<Boolean> = _updateUsernameStates.loadingState
    override val usernameStatus: Value<Status> = _updateUsernameStates.status
    override fun updateUsernameStatus(status: Status) { _updateUsernameStates.updateStatus(status) }
    override fun getUsernameAsResponse(): String? = with(_updateUsernameStates.status.value) {
        takeIf { it is Error }?.let {
            when {
                (it as Error).body is HttpResponse ->
                    if ((it.body as HttpResponse).status == BadRequest) USERNAME_EXISTS else null
                it.body is String -> it.body
                else -> null
            }
        }
    }
    override suspend fun updateUsername(update: UpdateUsernameRequest, context: CoroutineContext) =
        _updateUsernameStates.apiCall(update, context)

    private val _updatePasswordStates = instanceKeeper.getOrCreate { PasswordModelImpl(server) }
    override val passwordLoading: Value<Boolean> = _updatePasswordStates.loadingState
    override val passwordStatus: Value<Status> = _updatePasswordStates.status
    override suspend fun updatePassword(update: UpdatePasswordRequest, context: CoroutineContext) =
        _updatePasswordStates.apiCall(update, context)

    private val _deleteAccountStates = instanceKeeper.getOrCreate { DeleteModelImpl(server) }
    override val deleteAccountLoading: Value<Boolean> = _deleteAccountStates.loadingState
    override val deleteAccountStatus: Value<Status> = _deleteAccountStates.status
    override suspend fun deleteAccount(delete: Boolean, context: CoroutineContext) = withContext(context) {
        async { _deleteAccountStates.apiCall(delete, context) }.await()
        if (_deleteAccountStates.status.value == Success) settings.clear()
    }

    @Serializable
    private data object DeleteAccountWarningConfig
}