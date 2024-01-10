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
    fun onReceivedRequestsSelected()
    fun navBack()

    sealed class Child {
        class SentChild(val component: SentRequestsComponent) : Child()
        class ReceivedChild(val component: ReceivedRequestsComponent) : Child()
    }
    // TODO: 3. Requests
    //  GET /friend_request/sent_friend_requests - see the list of friends requests a user has SENT
    //  POST /friend_request/cancel_request - remove a friend request you've sent
    //  GET /friend_request/received_friend_requests - list of friend requests a user has RECEIVED
    //  POST /friend_request - action to accept or reject a received friend request
}