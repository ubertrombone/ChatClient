package component.register

import api.ApplicationApi
import com.arkivanov.decompose.value.Value
import util.MainPhases
import util.Username

interface RegisterComponent {
    val title: String
    
    val server: ApplicationApi
    
    val pushTo: (MainPhases) -> Unit
}