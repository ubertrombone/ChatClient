package component.login

import com.arkivanov.decompose.value.Value
import util.MainPhases
import util.Username

interface LoginComponent {
    val title: String
    val token: String
    val username: Value<Username>
    val password: Value<String>
    
    val server: String //TODO: This should be a custom server class
    
    fun pushTo(phase: MainPhases)
}