package component.login

import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class UsernameModelImpl(initialState: String) : InstanceKeeper.Instance, UsernameModel {
    override val scope = CoroutineScope(Dispatchers.Main)
    override val username = MutableValue(initialState)

    override fun update(usernameTo: String) { scope.launch { username.update { usernameTo } } }
    override fun onDestroy() { scope.cancel() }
}