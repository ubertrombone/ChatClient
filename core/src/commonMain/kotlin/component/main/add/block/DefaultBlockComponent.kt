package component.main.add.block

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import component.main.add.model.Friends
import kotlinx.serialization.builtins.serializer
import util.Status
import util.Status.Loading
import util.Username

class DefaultBlockComponent(
    componentContext: ComponentContext,
    override val server: ApplicationApi,
    override val dismiss: () -> Unit,
    override val logout: () -> Unit
) : BlockComponent, ComponentContext by componentContext {

    private val _listStates = instanceKeeper.getOrCreate(BLOCK_LIST_KEY) {
        BlockListModel(
            initialLoadingState = stateKeeper.consume(key = LOADING_LIST_KEY, strategy = Boolean.serializer()) ?: true,
            initialStatus = stateKeeper.consume(key = STATUS_LIST_KEY, strategy = Status.serializer()) ?: Loading,
            initialState = stateKeeper.consume(key = BLOCK_LIST_KEY, strategy = Friends.serializer()) ?: Friends(),
            server = server,
            authCallback = logout
        )
    }
    override val listStatus: Value<Status> = _listStates.status
    override val listLoading: Value<Boolean> = _listStates.loadingState
    override val blockList: Value<Friends> = _listStates.result

    private val _actionStates = instanceKeeper.getOrCreate {
        BlockModel(
            initialLoadingState = false,
            initialStatus = Loading,
            server = server,
            authCallback = logout
        )
    }
    override val actionStatus: Value<Status> = _actionStates.status
    override val actionLoading: Value<Boolean> = _actionStates.loadingState

    override fun getBlockList() = _listStates.getBlockList()

    override fun unblock(user: Username) = _actionStates.unblock(user)

    init {
        stateKeeper.register(key = BLOCK_LIST_KEY, strategy = Friends.serializer()) { _listStates.result.value }
        stateKeeper.register(key = LOADING_LIST_KEY, strategy = Boolean.serializer()) { _listStates.loadingState.value }
        stateKeeper.register(key = STATUS_LIST_KEY, strategy = Status.serializer()) { _listStates.status.value }
    }

    private companion object {
        const val BLOCK_LIST_KEY = "BLOCK_LIST_KEY"
        const val LOADING_LIST_KEY = "LOADING_LIST_KEY"
        const val STATUS_LIST_KEY = "STATUS_LIST_KEY"
    }
}