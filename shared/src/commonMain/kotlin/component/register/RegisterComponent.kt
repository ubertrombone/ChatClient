package component.register

import api.ApplicationApi
import api.model.AccountRequest
import com.arkivanov.decompose.value.Value
import settings.SettingsRepository
import util.MainPhases
import util.Status

interface RegisterComponent {
    val settings: SettingsRepository
    val server: ApplicationApi
    val pushTo: (MainPhases) -> Unit

    val title: String
    val isLoading: Value<Boolean>
    val registrationStatus: Value<Status>
    val usernameStatus: Value<Status>
    val passwordStatus: Value<Status>
    val rememberMe: Value<Boolean>

    fun updateRegistration(status: Status)
    fun update(rememberMe: Boolean)
    suspend fun validateUsername(username: String): Boolean
    suspend fun validatePassword(password: String, confirmation: String): Boolean
    suspend fun register(account: AccountRequest)
    fun validateCredentials(username: String, password: String, confirmation: String)
}