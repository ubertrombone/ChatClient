package api

import api.model.ChatMessage
import api.model.SendChatResponse
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
import io.ktor.websocket.*
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
    private val json = Json { prettyPrint = true }

    private val _userInput = userInput
    suspend fun emitInput(message: ChatMessage) = _userInput.emit(message)

    private val _incomingMessages = MutableSharedFlow<ChatMessage>()
    val incomingMessages: SharedFlow<ChatMessage> = _incomingMessages

    private val _response = MutableSharedFlow<SendChatResponse>()
    val response: SharedFlow<SendChatResponse> = _response

    init {
        scope.launch {
            client.webSocket(path = "/chat", request = { bearerAuth(settings.token.get()) }) {
                val incomingMessageRoutine = launch { incomingMessages() }
                val userInputRoutine = launch { inputMessages() }

                userInputRoutine.join()
                incomingMessageRoutine.cancelAndJoin()
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.incomingMessages() {
        while (scope.isActive) {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    runCatching { _incomingMessages.emit(json.decodeFromString(text)) }.getOrElse {
                        runCatching {
                            _response.emit(json.decodeFromString<SendChatResponse>(text))
                            Napier.i("RESPONSE ${json.decodeFromString<SendChatResponse>(text)}", tag = "SEND RESPONSE")
                        }.getOrElse {
                            Napier.e(it.message ?: "An unknown error has occurred.", throwable = it)
                        }
                    }
                }
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.inputMessages() {
        while (scope.isActive) {
            _userInput.collect {
                runCatching { it?.let { sendSerialized(it) } }.getOrElse { throwable ->
                    Napier.e(throwable.message ?: "An unknown error has occurred.")
                }
            }
        }
    }

    override fun onDestroy() {
        println("DESTROYED")
        client.close()
        scope.cancel()
    }
}