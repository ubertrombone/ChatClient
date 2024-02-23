package api

import api.model.ChatEndPointResponse
import api.model.OpenChatRequest
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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import settings.SettingsRepository
import util.Constants

class ChatRequestApi(
    private val settings: SettingsRepository,
    private val chatRepository: ChatRepository
) : InstanceKeeper.Instance {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val json = Json { prettyPrint = true }
    private val client = HttpClient {
        install(WebSockets) { contentConverter = KotlinxWebsocketSerializationConverter(Json) }
        install(ContentNegotiation) { json(json) }
        defaultRequest { url(scheme = "ws", host = Constants.IP, port = Constants.PORT) }
    }

    private val _chats: MutableSharedFlow<ChatApi> = MutableSharedFlow()
    val chats = _chats.asSharedFlow()

    private val _chatRequest = MutableSharedFlow<OpenChatRequest?>()
    suspend fun emitRequest(request: OpenChatRequest) = _chatRequest.emit(request)

    init {
        scope.launch {
            client.webSocket(path = "/chat", request = { bearerAuth(settings.token.get()) }) {
                val incomingRequestRoutine = launch { incomingRequest() }
                val outgoingRequestRoutine = launch { outgoingRequest() }

                outgoingRequestRoutine.join()
                incomingRequestRoutine.cancelAndJoin()
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.incomingRequest() {
        while (scope.isActive) {
            incoming.consumeEach { frame ->
                if (frame is Frame.Text) runCatching {
                    json.decodeFromString<ChatEndPointResponse>(frame.readText()).apply {
                        Napier.i { "Request Response: $this" }
                        _chats.emit(ChatApi(chatId, settings, chatRepository))
                    }
                }.getOrElse { Napier.e(it.message ?: "An unknown error has occurred.", throwable = it) }
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.outgoingRequest() {
        while (scope.isActive) {
            _chatRequest.collect {
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