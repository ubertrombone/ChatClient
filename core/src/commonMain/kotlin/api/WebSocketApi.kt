package api

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import settings.SettingsRepository
import util.Constants.IP
import util.Constants.PORT

class WebSocketApi(private val settings: SettingsRepository) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val client = HttpClient {
        install(WebSockets)
        install(ContentNegotiation) { json(json = Json { prettyPrint = true }) }
        defaultRequest { url(scheme = "ws", host = IP, port = PORT) }
    }

    init {
        scope.launch {
            client.webSocket(method = Get, path = "/chat") {

            }
        }
    }

    suspend fun DefaultClientWebSocketSession.outputMessages() {

    }

    suspend fun DefaultClientWebSocketSession.inputMessages() {
        while (true) {

        }
    }

    override fun onDestroy() = scope.cancel()
}