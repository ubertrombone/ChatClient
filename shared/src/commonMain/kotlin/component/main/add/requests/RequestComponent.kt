package component.main.add.requests

import api.ApplicationApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import component.main.add.requests.received.ReceivedRequestsComponent
import component.main.add.requests.sent.SentRequestsComponent

interface RequestComponent {
    val server: ApplicationApi
    val dismiss: () -> Unit
    val logout: () -> Unit

    val childStack: Value<ChildStack<*, Child>>

    fun onSentRequestsSelected()
    fun navBack()

    sealed class Child {
        class SentChild(val component: SentRequestsComponent) : Child()
        class ReceivedChild(val component: ReceivedRequestsComponent) : Child()
    }
}