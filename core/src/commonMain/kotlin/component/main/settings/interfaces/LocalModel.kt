package component.main.settings.interfaces

import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.CoroutineScope
import util.Status
import kotlin.coroutines.CoroutineContext

interface LocalModel {
    val scope: CoroutineScope
    val loadingState: MutableValue<Boolean>
    val status: MutableValue<Status>

    suspend fun get(context: CoroutineContext)
    suspend fun <T> update(value: T, context: CoroutineContext)
}