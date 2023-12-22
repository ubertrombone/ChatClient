package component.login

import api.ApplicationApi
import api.model.AuthenticationRequest
import com.arkivanov.decompose.value.Value
import settings.SettingsRepository
import util.MainPhases
import util.Status

interface LoginComponent {
    val title: String
    val settings: SettingsRepository
    val server: ApplicationApi
    val pushTo: (MainPhases) -> Unit

    val initStatus: Value<Status>
    val loginStatus: Value<Status>
    val rememberMe: Value<Boolean>
    val isInitLoading: Value<Boolean>
    val isLoading: Value<Boolean>

    fun updateInit(status: Status)
    fun updateLogin(status: Status)
    fun update(rememberMe: Boolean)
    fun initLogin()
    fun login(credentials: AuthenticationRequest)
}