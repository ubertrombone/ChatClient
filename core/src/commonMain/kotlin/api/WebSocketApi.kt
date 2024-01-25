package api

import api.model.ChatMessage
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import db.ChatRepository
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.serialization.json.Json
import settings.SettingsRepository
import util.Constants.IP
import util.Constants.PORT

class WebSocketApi(
    userInput: MutableSharedFlow<ChatMessage?>,
    private val settings: SettingsRepository,
    private val chatRepository: ChatRepository
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val client = HttpClient {
        install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(Json) }
        install(ContentNegotiation) { json(json = Json { prettyPrint = true }) }
        defaultRequest { url(scheme = "ws", host = IP, port = PORT) }
    }

    private val _userInput = userInput
    suspend fun emitInput(message: ChatMessage) = _userInput.emit(message)

    private val _incomingMessages = MutableSharedFlow<ChatMessage>()
    val incomingMessages: SharedFlow<ChatMessage> = _incomingMessages

    init {
        scope.launch {
            client.webSocket(path = "/chat", request = { bearerAuth(settings.token.get()) }) {
                val incomingMessageRoutine = launch { outputMessages() }
                val userInputRoutine = launch { inputMessages() }

                userInputRoutine.join()
                incomingMessageRoutine.cancelAndJoin()
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.outputMessages() {
        try {
            incoming.consumeEach {
                val m = receiveDeserialized<ChatMessage>()
                println("MESSAGE: $m")
                _incomingMessages.emit(m)
            }
        } catch (e: Exception) { println("ERROR: ${e.message}") }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        while (true) {
            _userInput.collect {
                println("INPUT: $it")
                runCatching { it?.let { sendSerialized(it) } }.getOrElse { throwable ->
                    Napier.e(throwable.message ?: "An unknown error has occurred.")
                }
            }
        }
    }


    override fun onDestroy() {
        client.close()
        scope.cancel()
    }
}