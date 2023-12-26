package api

import api.model.*
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.Accepted
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import settings.SettingsRepository
import util.Status
import util.Status.Error
import util.Status.Success
import util.Username
import kotlin.coroutines.coroutineContext

class ApplicationApiImpl(private val settings: SettingsRepository) : InstanceKeeper.Instance, ApplicationApi {
    private val scope = CoroutineScope(Dispatchers.IO)

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(json = Json { prettyPrint = true })
        }

//        install(Auth) {
//            bearer {
//                loadTokens { bearerTokens.value } // TODO: Encrypt token
//            }
//        }

        defaultRequest {
            url("http://192.168.0.10:8080")
        }
    }

    override suspend fun register(account: AccountRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/register", body = account) { response ->
            if (response.status == Conflict) Error(response.bodyAsText())
            else response.body<SimpleResponse>().run {
                if (successful) {
                    settings.apply {
                        token.set(message)
                        username.set(account.username.name)
                        password.set(account.password) // TODO: Encrypt password in local storage
                    }
                    Success
                } else Error(message)
            }
        }
    }

    override suspend fun login() = withContext(scope.coroutineContext) {
        client.get("/login") { bearerAuth(settings.token.get()) }
            .let {
                when (it.status) {
                    OK -> Success
                    Unauthorized -> Error("Token is not valid or has expired")
                    else -> Error("Unknown error - the server may be down. Try logging in again later.")
                }
            }
    }

    override suspend fun authenticate(credentials: AuthenticationRequest) = withContext(scope.coroutineContext) {
        client.post("/authenticate") {
            contentType(Json)
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
        client.get("/logout") { bearerAuth(settings.token.get()) }
            .let { if (it.status == OK) Success else Error("There was a problem logging out!") }
    }

    override suspend fun getFriends() = withContext(scope.coroutineContext) {
        with (client.get("/friends") { bearerAuth(settings.token.get()) }) {
            if (status == OK) body<Set<FriendInfo>>() else null
        }
    }

    override suspend fun add(friend: Username) = withContext(coroutineContext) {
        client.post("/friends/add") {
            contentType(Json)
            setBody(friend)
            bearerAuth(settings.token.get())
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
            contentType(Json)
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
            contentType(Json)
            setBody(to)
        }.let { if (it.status == Accepted) Success else Error(it.bodyAsText()) }
    }

    override suspend fun cancelFriendRequest(to: Username) = withContext(coroutineContext) {
        client.post("/friend_request/cancel_request") {
            contentType(Json)
            setBody(to)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getBlockList() = withContext(scope.coroutineContext) {
        with (client.get("/block")) { if (status == OK) body<Set<Username>>() else null }
    }

    override suspend fun block(user: Username) = withContext(scope.coroutineContext) {
        client.post("/block") {
            contentType(Json)
            setBody(user)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun unblock(user: Username) = withContext(scope.coroutineContext) {
        client.post("/block/unblock") {
            contentType(Json)
            setBody(user)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getGroupChats() = withContext(scope.coroutineContext) {
        with (client.get("/group_chat")) { if (status == OK) body<Set<GroupChat>>() else null }
    }
    
    override suspend fun createGroupChat(name: GroupChatNameRequest) = withContext(scope.coroutineContext) {
        client.post("/group_chat") {
            contentType(Json)
            setBody(name)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getStatus() = withContext(scope.coroutineContext) {
        with (client.get("/status")) { if (status == OK) body<String?>() else null }
    }

    override suspend fun update(status: StatusRequest) = withContext(scope.coroutineContext) {
        client.post("/status") {
            contentType(Json)
            setBody(status)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getCache() = withContext(scope.coroutineContext) {
        with (client.get("/settings/cache")) { if (status == OK) body<Boolean>() else null }
    }

    override suspend fun update(password: UpdatePasswordRequest) = withContext(scope.coroutineContext) {
        client.post("/settings/updatepwd") {
            contentType(Json)
            setBody(password)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun update(username: UpdateUsernameRequest) = withContext(scope.coroutineContext) {
        client.post("/settings/updateuser") {
            contentType(Json)
            setBody(username)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun update(cache: Boolean) = withContext(scope.coroutineContext) {
        client.post("/settings/cache") {
            contentType(Json)
            setBody(cache)
        }.let {
            if (it.status == OK) Success.also { settings.cache.set(cache) }
            else Error(it.bodyAsText())
        }
    }

    override suspend fun deleteAccount(decision: Boolean) = withContext(scope.coroutineContext) {
        client.post("/settings/delete") {
            contentType(Json)
            setBody(decision)
        }.let { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    private suspend inline fun <reified T> postHelper( // TODO: Study inline fun and reified type generics
        route: String,
        body: T,
        crossinline operation: suspend (HttpResponse) -> Status // TODO: Study crossinline parameters
    ): Status =
        withContext(scope.coroutineContext) {
            client.post(route) {
                contentType(Json)
                setBody(body)
                bearerAuth(settings.token.get())
            }.let { operation(it) }
        }

    override fun onDestroy() {
        scope.cancel()
        client.close()
    }
}