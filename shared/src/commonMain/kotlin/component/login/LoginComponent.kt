package component.login

import com.arkivanov.decompose.value.Value
import util.MainPhases
import util.Username

interface LoginComponent {
    val title: String
    val token: String
    
    val server: String //TODO: This should be a custom server class
    
    val pushTo: (MainPhases) -> Unit
}