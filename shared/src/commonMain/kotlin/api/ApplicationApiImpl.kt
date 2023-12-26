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
        getHelper("/login") {
            when (status) {
                OK -> Success
                Unauthorized -> Error("Token is not valid or has expired")
                else -> Error("Unknown error - the server may be down. Try logging in again later.")
            }
        }
    }

    override suspend fun authenticate(credentials: AuthenticationRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/authenticate", body = credentials) { response ->
            when (response.status) {
                OK -> Success.also { settings.token.set(response.bodyAsText()) }
                BadRequest -> Error(response.bodyAsText())
                else -> Error("Unknown error - the server may be down. Try logging in again later.")
            }
        }
    }

    override suspend fun logout() = withContext(scope.coroutineContext) {
        getHelper("/logout") { if (status == OK) Success else Error("There was a problem logging out!") }
    }

    override suspend fun getFriends() = withContext(scope.coroutineContext) {
        getHelper("/friends") { if (status == OK) body<Set<FriendInfo>>() else null }
    }

    override suspend fun add(friend: Username) = withContext(coroutineContext) {
        postHelper(route = "/friends/add", body = friend) { response ->
            when (response.status) {
                OK -> Success
                UnprocessableEntity -> Error(response.bodyAsText())
                else -> Error("Unknown error.\n- Friend username may not exist or account has been deleted.\n- Server may be down.")
            }
        }
    }

    override suspend fun remove(friend: Username) = withContext(coroutineContext) {
        postHelper(route = "/friends/remove", body = friend) { response ->
            when (response.status) {
                OK -> Success
                UnprocessableEntity -> Error(response.bodyAsText())
                else -> Error("Unknown error.\n- Friend may not exist or account has been deleted.\n- Server may be down.")
            }
        }
    }

    override suspend fun getSentFriendRequests() = withContext(scope.coroutineContext) {
        getHelper("/friend_request/sent_friend_requests") { if (status == OK) body<Set<FriendRequest>>() else null }
    }

    override suspend fun getReceivedFriendRequests() = withContext(scope.coroutineContext) {
        getHelper("/friend_request/received_friend_requests") { if (status == OK) body<Set<FriendRequest>>() else null }
    }

    override suspend fun sendFriendRequest(to: Username) = withContext(coroutineContext) {
        postHelper(route = "/friend_request", body = to) { if (it.status == Accepted) Success else Error(it.bodyAsText()) }
    }

    override suspend fun cancelFriendRequest(to: Username) = withContext(coroutineContext) {
        postHelper(route = "/friend_request/cancel_request", body = to) { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getBlockList() = withContext(scope.coroutineContext) {
        getHelper("/block") { if (status == OK) body<Set<Username>>() else null }
    }

    override suspend fun block(user: Username) = withContext(scope.coroutineContext) {
        postHelper(route = "/block", body = user) { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun unblock(user: Username) = withContext(scope.coroutineContext) {
        postHelper(route = "/block/unblock", body = user) { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getGroupChats() = withContext(scope.coroutineContext) {
        getHelper("group_chat") { if (status == OK) body<Set<GroupChat>>() else null }
    }
    
    override suspend fun createGroupChat(name: GroupChatNameRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/group_chat", body = name) { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getStatus() = withContext(scope.coroutineContext) {
        getHelper("/status") { if (status == OK) body<String?>() else null }
    }

    override suspend fun update(status: StatusRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/status", body = status) { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun getCache() = withContext(scope.coroutineContext) {
        getHelper("/settings/cache") { if (status == OK) body<Boolean>() else null }
    }

    override suspend fun update(password: UpdatePasswordRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/settings/updatepwd", body = password) { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun update(username: UpdateUsernameRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/settings/updateuser", body = username) { if (it.status == OK) Success else Error(it.bodyAsText()) }
    }

    override suspend fun update(cache: Boolean) = withContext(scope.coroutineContext) {
        postHelper(route = "/settings/cache", body = cache) {
            if (it.status == OK) Success.also { settings.cache.set(cache) } else Error(it.bodyAsText())
        }
    }

    override suspend fun deleteAccount(decision: Boolean) = withContext(scope.coroutineContext) {
        postHelper(route = "/settings/delete", body = decision) { if (it.status == OK) Success else Error(it.bodyAsText()) }
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

    private suspend fun <T> getHelper(route: String, operation: suspend HttpResponse.() -> T): T =
        withContext(scope.coroutineContext) {
            with(client.get(route) { bearerAuth(settings.token.get()) }) { operation(this) }
        }

    override fun onDestroy() {
        scope.cancel()
        client.close()
    }
}