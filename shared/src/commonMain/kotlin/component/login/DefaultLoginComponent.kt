package component.login

import api.ApplicationApi
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import util.MainPhases
import util.Status
import util.Status.Loading

class DefaultLoginComponent(
    componentContext: ComponentContext,
    override val token: String,
    override val server: ApplicationApi,
    override val pushTo: (MainPhases) -> Unit
) : LoginComponent, ComponentContext by componentContext {
    val scope = CoroutineScope(Dispatchers.Main)

    override val title: String = "Login"

    private val _status: MutableValue<Status> = MutableValue(Loading)
    override val status: Value<Status> = _status

    override fun update(status: Status){
        scope.launch { _status.update { status } }
    }
}