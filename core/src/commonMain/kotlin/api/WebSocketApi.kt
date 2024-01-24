package api

import api.model.ChatMessage
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import db.ChatRepository
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json
import settings.SettingsRepository
import util.Constants.IP
import util.Constants.PORT

class WebSocketApi(
    private val userInput: StateFlow<ChatMessage>,
    private val settings: SettingsRepository,
    private val chatRepository: ChatRepository
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val client = HttpClient {
        install(WebSockets)
        install(ContentNegotiation) { json(json = Json { prettyPrint = true }) }
        defaultRequest { url(scheme = "ws", host = IP, port = PORT) }
    }

    private val _incomingMessages = MutableSharedFlow<ChatMessage>()
    val incomingMessages: SharedFlow<ChatMessage> = _incomingMessages

    init {
        scope.launch {
            client.webSocket(method = Get, path = "/chat") {
                val incomingMessageRoutine = launch { outputMessages() }
                val userInputRoutine = launch { inputMessages() }

                userInputRoutine.join()
                incomingMessageRoutine.cancelAndJoin()
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.outputMessages() {
        try {
            for (message in incoming) {
                // TODO: Is this deserializing anything?
                _incomingMessages.emit(receiveDeserialized<ChatMessage>().also(::println))
            }
        } catch (e: Exception) { println("ERROR: ${e.message}") }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages(): Unit =
        userInput.collect { runCatching { sendSerialized(it) }.getOrNull() }


    override fun onDestroy() {
        client.close()
        scope.cancel()
    }
}