package component.main.settings.status

import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.CoroutineScope
import util.Status
import kotlin.coroutines.CoroutineContext

interface StatusModel {
    val scope: CoroutineScope
    val loadingState: MutableValue<Boolean>
    val statusStatus: MutableValue<Status>

    suspend fun getStatus(context: CoroutineContext)
    suspend fun updateStatus(status: String, context: CoroutineContext)
}