package ui.composables.states

import com.arkivanov.decompose.value.Value

interface AuthenticationFieldState {
    val input: Value<String>
    val isValid: Value<Boolean>
    fun updateInput(with: String)
}