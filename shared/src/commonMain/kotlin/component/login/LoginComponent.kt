package component.login

import api.ApplicationApi
import com.arkivanov.decompose.value.Value
import util.MainPhases
import util.Status
import util.Username

interface LoginComponent {
    val title: String
    val token: String
    val server: ApplicationApi
    val pushTo: (MainPhases) -> Unit

    val status: Value<Status>
    val username: Value<String>

    fun update(status: Status)
    fun update(username: String)
}