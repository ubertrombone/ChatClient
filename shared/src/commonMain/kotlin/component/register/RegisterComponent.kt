package component.register

import com.arkivanov.decompose.value.Value
import util.MainPhases
import util.Username

interface RegisterComponent {
    val title: String
    val username: Value<Username>
    val password: Value<String>
    
    val server: String //TODO: This should be a custom server class
    
    fun pushTo(phase: MainPhases)
}