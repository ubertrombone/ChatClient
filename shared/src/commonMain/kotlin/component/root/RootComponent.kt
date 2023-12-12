package component.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import component.login.LoginComponent
import component.main.MainComponent
import component.register.RegisterComponent

interface RootComponent {
    val root: String

    val childStack: Value<ChildStack<*, Child>>

    fun onBackPressed()

    sealed class Child {
        class MainChild(val component: MainComponent) : Child()
        class RegisterChild(val component: RegisterComponent) : Child()
        class LoginChild(val component: LoginComponent) : Child()
    }
}