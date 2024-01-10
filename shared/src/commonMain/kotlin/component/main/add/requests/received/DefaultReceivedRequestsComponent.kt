package component.main.add.requests.received

import com.arkivanov.decompose.ComponentContext

class DefaultReceivedRequestsComponent(
    componentContext: ComponentContext
) : ReceivedRequestsComponent, ComponentContext by componentContext