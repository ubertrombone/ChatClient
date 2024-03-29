package component.main.add.requests

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import component.main.add.requests.DefaultRequestComponent.Config.Received
import component.main.add.requests.DefaultRequestComponent.Config.Sent
import component.main.add.requests.RequestComponent.Child
import component.main.add.requests.RequestComponent.Child.ReceivedChild
import component.main.add.requests.RequestComponent.Child.SentChild
import component.main.add.requests.received.DefaultReceivedRequestsComponent
import component.main.add.requests.received.ReceiveListModel
import component.main.add.requests.sent.DefaultSentRequestsComponent
import kotlinx.serialization.Serializable

class DefaultRequestComponent(
    componentContext: ComponentContext,
    override val receivedListModel: ReceiveListModel,
    override val server: ApplicationApi,
    override val dismiss: () -> Unit,
    override val logout: () -> Unit
) : RequestComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private val _childStack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Received,
        handleBackButton = true,
        childFactory = ::createChild
    )
    override val childStack: Value<ChildStack<*, Child>> = _childStack

    private fun createChild(
        config: Config,
        componentContext: ComponentContext
    ) = when (config) {
        Sent -> SentChild(sentChild(componentContext))
        Received -> ReceivedChild(receivedChild(componentContext))
    }

    private fun sentChild(componentContext: ComponentContext) = DefaultSentRequestsComponent(
        componentContext = componentContext,
        server = server,
        navBack = ::navBack,
        dismiss = dismiss,
        logout = logout
    )

    private fun receivedChild(componentContext: ComponentContext) = DefaultReceivedRequestsComponent(
        componentContext = componentContext,
        server = server,
        receiveListModel = receivedListModel,
        navToSent = ::onSentRequestsSelected,
        dismiss = dismiss,
        logout = logout
    )

    override fun onSentRequestsSelected() = navigation.bringToFront(Sent)
    override fun onReceivedRequestsSelected() = navigation.bringToFront(Received)

    override fun navBack() = navigation.pop()

    @Serializable
    private sealed class Config {
        @Serializable data object Sent : Config()
        @Serializable data object Received : Config()
    }
}