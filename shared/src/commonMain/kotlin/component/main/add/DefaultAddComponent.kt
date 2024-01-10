package component.main.add

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.add.block.BlockComponent
import component.main.add.block.DefaultBlockComponent
import component.main.add.model.Friends
import component.main.add.requests.DefaultRequestComponent
import component.main.add.requests.RequestComponent
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import util.Status
import util.Status.Success
import util.Username

// TODO: Test logout callbacks
class DefaultAddComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val logout: () -> Unit
) : AddComponent, ComponentContext by componentContext {

    private val blockNavigation = SlotNavigation<BlockConfig>()
    private val _blockSlot = childSlot(
        source = blockNavigation,
        serializer = BlockConfig.serializer(),
        key = "BLOCK_SLOT",
        handleBackButton = true
    ) { _, childComponentContext ->
        DefaultBlockComponent(
            childComponentContext,
            server = server,
            dismiss = ::dismissBlock,
            logout = logout
        )
    }
    override val blockSlot: Value<ChildSlot<*, BlockComponent>> = _blockSlot
    override fun showBlock() = blockNavigation.activate(BlockConfig)
    override fun dismissBlock() = blockNavigation.dismiss()

    private val requestNavigation = SlotNavigation<RequestConfig>()
    private val _requestSlot = childSlot(
        source = requestNavigation,
        serializer = RequestConfig.serializer(),
        key = "REQUEST_SLOT",
        handleBackButton = true
    ) { _, childComponentContext ->
        DefaultRequestComponent(
            childComponentContext,
            server = server,
            dismiss = requestNavigation::dismiss,
            logout = logout
        )
    }
    override val requestSlot: Value<ChildSlot<*, RequestComponent>> = _requestSlot
    override fun showRequest() = requestNavigation.activate(RequestConfig)
    override fun dismissRequest() = requestNavigation.dismiss()

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

    init {
        stateKeeper.register(key = QUERY_KEY, strategy = Friends.serializer()) { _queryState.result.value }
        stateKeeper.register(key = QUERY_LOAD_KEY, strategy = Boolean.serializer()) { _queryState.loadingState.value }
        stateKeeper.register(key = QUERY_STATUS_KEY, strategy = Status.serializer()) { _queryState.status.value }
    }

    @Serializable
    data object BlockConfig
    @Serializable
    data object RequestConfig

    private companion object {
        const val QUERY_KEY = "QUERY_KEY"
        const val QUERY_LOAD_KEY = "QUERY_LOAD_KEY"
        const val QUERY_STATUS_KEY = "QUERY_STATUS_KEY"
    }
}