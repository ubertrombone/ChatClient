package api

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class ApplicationApiImpl : InstanceKeeper.Instance, ApplicationApi {
    override val scope = CoroutineScope(Dispatchers.Main)
    override val client = HttpClient {}

    override fun onDestroy() {
        scope.cancel()
        client.close()
    }
}