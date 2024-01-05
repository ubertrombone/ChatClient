package component.main.settings.interfaces

import com.arkivanov.decompose.value.MutableValue
import util.Status
import kotlin.coroutines.CoroutineContext

interface ApiModel {
    val loadingState: MutableValue<Boolean>
    val status: MutableValue<Status>

    fun updateStatus(value: Status)
    suspend fun <T> apiCall(value: T, context: CoroutineContext)
}