package component.root

import api.ApplicationApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import component.login.LoginComponent
import component.main.MainComponent
import component.register.RegisterComponent
import db.ChatRepository

interface RootComponent {
    val chatRepository: ChatRepository
    val server: ApplicationApi
    val childStack: Value<ChildStack<*, Child>>

    fun onBackPressed()

    sealed class Child {
        class MainChild(val component: MainComponent) : Child()
        class RegisterChild(val component: RegisterComponent) : Child()
        class LoginChild(val component: LoginComponent) : Child()
    }
}