package component.login

import api.ApplicationApi
import com.arkivanov.decompose.value.Value
import settings.SettingsRepository
import util.MainPhases
import util.Status
import util.Username

interface LoginComponent {
    val title: String
    val settings: SettingsRepository
    val server: ApplicationApi
    val pushTo: (MainPhases) -> Unit

    val status: Value<Status>
    val username: Value<String>
    val rememberMe: Value<Boolean>

    fun update(status: Status)
    fun update(username: String)
    fun update(rememberMe: Boolean)
    fun login()
}