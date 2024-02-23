package api

import api.model.FriendChatMessage
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.json.Json
import settings.SettingsRepository
import util.Constants

class ChatApi(
    val chatId: Int,
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

    private val _unreadMessageCount = MutableStateFlow(0)
    val unreadMessageCount = _unreadMessageCount.asSharedFlow()

    private val _userInput = MutableSharedFlow<FriendChatMessage?>()
    suspend fun emitInput(message: FriendChatMessage) = _userInput.emit(message)

    init {
        scope.launch {
            client.webSocket(path = "/chat/$chatId", request = { bearerAuth(settings.token.get()) }) {
                val incomingMessagesRoutine = launch { incomingMessages() }
                val outgoingMessagesRoutine = launch { outgoingMessages() }

                outgoingMessagesRoutine.join()
                incomingMessagesRoutine.cancelAndJoin()
            }
        }
    }

    private suspend fun DefaultClientWebSocketSession.incomingMessages() {
        while (scope.isActive) {

        }
    }

    private suspend fun DefaultClientWebSocketSession.outgoingMessages() {
        while (scope.isActive) {
            _userInput.collect {
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