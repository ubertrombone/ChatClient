package component.login

import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.CoroutineScope

interface UsernameModel {
    val scope: CoroutineScope
    val username: MutableValue<String>
    
    fun update(usernameTo: String)
}