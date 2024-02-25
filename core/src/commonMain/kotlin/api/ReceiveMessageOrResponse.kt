package api

import api.model.FriendChatMessage
import api.model.FriendChatResponse
import io.github.aakira.napier.Napier
import kotlinx.serialization.json.Json

suspend fun receiveMessageOrResponse(
    frame: String,
    json: Json,
    message: suspend (FriendChatMessage) -> Unit,
    response: suspend (FriendChatResponse) -> Unit
): Unit = runCatching { message(json.decodeFromString<FriendChatMessage>(frame)) }
    .getOrElse {
        runCatching {
            response(json.decodeFromString<FriendChatResponse>(frame))
            Napier.i("RESPONSE ${json.decodeFromString<FriendChatResponse>(frame)}", tag = "SEND RESPONSE")
        }.getOrElse { Napier.e(it.message ?: "An unknown error has occurred.", throwable = it) }
    }