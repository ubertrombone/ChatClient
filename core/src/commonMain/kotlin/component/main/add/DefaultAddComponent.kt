package component.main.add

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.add.AddComponent.Config
import component.main.add.AddComponent.Config.BlockConfig
import component.main.add.AddComponent.Config.RequestConfig
import component.main.add.AddComponent.Slots
import component.main.add.AddComponent.Slots.BlockList
import component.main.add.AddComponent.Slots.Requests
import component.main.add.block.BlockComponent
import component.main.add.block.DefaultBlockComponent
import component.main.add.model.Friends
import component.main.add.requests.DefaultRequestComponent
import component.main.add.requests.RequestComponent
import component.main.add.requests.received.ReceiveListModel
import kotlinx.serialization.builtins.serializer
import util.Status
import util.Status.Success
import util.Username

class DefaultAddComponent(
    componentContext: ComponentContext,
    override val receiveListModel: ReceiveListModel,
    override val server: ApplicationApi,
    override val logout: () -> Unit
) : AddComponent, ComponentContext by componentContext {

    private val navigation = SlotNavigation<Config>()
    private val _slots = childSlot(
        source = navigation,
        serializer = Config.serializer(),
        key = "SLOTS",
        handleBackButton = true
    ) { slot, childComponentContext ->
        when (slot) {
            BlockConfig -> BlockList(block(childComponentContext))
            RequestConfig -> Requests(requests(childComponentContext))
        }
    }
    override val slots: Value<ChildSlot<*, Slots>> = _slots

    private fun block(componentContext: ComponentContext): BlockComponent = DefaultBlockComponent(
        componentContext = componentContext,
        server = server,
        dismiss = navigation::dismiss,
        logout = logout
    )

    private fun requests(componentContext: ComponentContext): RequestComponent = DefaultRequestComponent(
        componentContext = componentContext,
        receivedListModel = receiveListModel,
        server = server,
        dismiss = navigation::dismiss,
        logout = logout
    )

    override fun dismissSlot() = navigation.dismiss()
    override fun showSlot(config: Config) = navigation.activate(config)

    private val _queryState = instanceKeeper.getOrCreate(QUERY_KEY) {
        QueryModel(
            initialLoadingState = stateKeeper.consume(key = QUERY_LOAD_KEY, strategy = Boolean.serializer()) ?: false,
            initialStatus = stateKeeper.consume(key = QUERY_STATUS_KEY, strategy = Status.serializer()) ?: Success,
            initialState = stateKeeper.consume(key = QUERY_KEY, strategy = Friends.serializer()) ?: Friends(),
            server = server,
            authCallback = logout
        )
    }
    override val queryLoadingState: Value<Boolean> = _queryState.loadingState
    override val queryStatus: Value<Status> = _queryState.status
    override val query: Value<Friends> = _queryState.result

    private val _queryInput = MutableValue("")
    override val queryInput: Value<String> = _queryInput

    private val _actionState = instanceKeeper.getOrCreate {
        ActionModel(
            initialLoadingState = false,
            initialStatus = Success,
            server = server,
            authCallback = logout
        )
    }
    override val actionLoadingState: Value<Boolean> = _actionState.loadingState
    override val actionStatus: Value<Status> = _actionState.status

    override fun searchUsers(query: String) = _queryState.apiCall(query)

    override fun sendFriendRequest(to: Username) = _actionState.sendFriendRequest(to)

    override fun addFriend(username: Username) = _actionState.addFriend(username)

    override fun updateInput(input: String) = _queryInput.update { input }

    init {
        stateKeeper.register(key = QUERY_KEY, strategy = Friends.serializer()) { _queryState.result.value }
        stateKeeper.register(key = QUERY_LOAD_KEY, strategy = Boolean.serializer()) { _queryState.loadingState.value }
        stateKeeper.register(key = QUERY_STATUS_KEY, strategy = Status.serializer()) { _queryState.status.value }
    }

    private companion object {
        const val QUERY_KEY = "QUERY_KEY"
        const val QUERY_LOAD_KEY = "QUERY_LOAD_KEY"
        const val QUERY_STATUS_KEY = "QUERY_STATUS_KEY"
    }
}