package api

import androidx.compose.runtime.MutableState
import api.model.*
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import settings.SettingsRepository
import util.Status
import util.Status.*
import util.Username
import util.toUsername
import kotlin.coroutines.coroutineContext

class ApplicationApiImpl(override val settings: SettingsRepository) : InstanceKeeper.Instance, ApplicationApi {
    override val scope = CoroutineScope(Dispatchers.Main)
    override val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }

        install(Auth) {
            bearer {
                loadTokens { BearerTokens(settings.token.get(), "") } // TODO: Encrypt token
            }
        }

        defaultRequest {
            url("http://0.0.0.0:8080")
        }
    }

    override suspend fun register(account: AccountRequest) = withContext(scope.coroutineContext) {
        client.post("/register") {
            contentType(ContentType.Application.Json)
            setBody(account)
        }.let { response ->
            if (response.status == Conflict) Error(response.bodyAsText())
            else {
                val body = response.body<SimpleResponse>()
                if (body.successful) {
                    settings.apply {
                        token.set(body.message)
                        username.set(account.username.name)
                        password.set(account.password) // TODO: Encrypt password in local storage
                    }
                    Success
                } else Error(body.message)
            }
        }
    }

    override suspend fun login() = withContext(scope.coroutineContext) {
        when (client.get("/login").status) {
            OK -> Success
            Unauthorized -> authenticate(
                credentials = AuthenticationRequest(
                    username = settings.username.get().toUsername(),
                    password = settings.password.get()
                )
            )
            else -> Error("Unknown error - the server may be down. Try logging in again later.")
        }
    }

    override suspend fun authenticate(credentials: AuthenticationRequest) = withContext(scope.coroutineContext) {
        client.post("/authenticate") {
            contentType(ContentType.Application.Json)
            setBody(credentials)
        }.let { response ->
            when (response.status) {
                OK -> {
                    settings.token.set(response.bodyAsText())
                    Success
                }
                BadRequest -> Error(response.bodyAsText())
                else -> Error("Unknown error - the server may be down. Try logging in again later.")
            }
        }
    }

    override suspend fun logout() = withContext(scope.coroutineContext) {
        if (client.get("/logout").status == OK) Success else Error("There was a problem logging out!")
    }

    override suspend fun getFriends() = withContext(scope.coroutineContext) {
        with (client.get("/friends")) { if (status == OK) body<Set<FriendInfo>>() else null }
    }

    override suspend fun add(friend: Username) = withContext(coroutineContext) {
        client.post("/friends/add") {
            contentType(ContentType.Application.Json)
            setBody(friend)
        }.let { response ->
            when (response.status) {
                OK -> Success
                UnprocessableEntity -> Error(response.bodyAsText())
                else -> Error("Unknown error.\n- Friend username may not exist or account has been deleted.\n- Server may be down.")
            }
        }
    }

    override suspend fun remove(friend: Username) = withContext(coroutineContext) {
        client.post("/friends/remove") {
            contentType(ContentType.Application.Json)
            setBody(friend)
        }.let { response ->
            when (response.status) {
                OK -> Success
                UnprocessableEntity -> Error(response.bodyAsText())
                else -> Error("Unknown error.\n- Friend may not exist or account has been deleted.\n- Server may be down.")
            }
        }
    }

    override suspend fun getSentFriendRequests() = withContext(scope.coroutineContext) {
        with (client.get("/friend_request/sent_friend_requests")) {
            if (status == OK) body<Set<FriendRequest>>() else null
        }
    }

    override suspend fun getReceivedFriendRequests() = withContext(scope.coroutineContext) {
        with (client.get("/friend_request/received_friend_requests")) {
            if (status == OK) body<Set<FriendRequest>>() else null
        }
    }

    override suspend fun sendFriendRequest(to: Username) = withContext(coroutineContext) {
        client.post("/friend_request") {
            contentType(ContentType.Application.Json)
            setBody(to)
        }.let { if (it.status == Accepted) Success else Error(it.bodyAsText()) }
    }

    override suspend fun cancelFriendRequest(to: Username) = withContext(coroutineContext) {
        client.post("/friend_request/cancel_request") {
            contentType(ContentType.Application.Json)
            setBody(to)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getBlockList() = withContext(scope.coroutineContext) {
        with (client.get("/block")) { if (status == OK) body<Set<Username>>() else null }
    }

    override suspend fun block(user: Username) = withContext(scope.coroutineContext) {
        client.post("/block") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun unblock(user: Username) = withContext(scope.coroutineContext) {
        client.post("/block/unblock") {
            contentType(ContentType.Application.Json)
            setBody(user)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getGroupChats() = withContext(scope.coroutineContext) {
        with (client.get("/group_chat")) { if (status == OK) body<Set<GroupChat>>() else null }
    }
    
    override suspend fun createGroupChat(name: GroupChatNameRequest) = withContext(scope.coroutineContext) {
        client.post("/group_chat") {
            contentType(ContentType.Application.Json)
            setBody(name)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getStatus() = withContext(scope.coroutineContext) {
        with (client.get("/status")) { if (status == OK) body<String?>() else null }
    }

    override suspend fun update(status: StatusRequest) = withContext(scope.coroutineContext) {
        client.post("/status") {
            contentType(ContentType.Application.Json)
            setBody(status)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun update(password: UpdatePasswordRequest) = withContext(scope.coroutineContext) {
        client.post("/settings/updatepwd") {
            contentType(ContentType.Application.Json)
            setBody(password)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun update(username: UpdateUsernameRequest) = withContext(scope.coroutineContext) {
        client.post("/settings/updateuser") {
            contentType(ContentType.Application.Json)
            setBody(username)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun update(cache: Boolean) = withContext(scope.coroutineContext) {
        client.post("/settings/cache") {
            contentType(ContentType.Application.Json)
            setBody(cache)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun deleteAccount(decision: Boolean) = withContext(scope.coroutineContext) {
        client.post("/settings/delete") {
            contentType(ContentType.Application.Json)
            setBody(decision)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override fun onDestroy() {
        scope.cancel()
        client.close()
    }
}