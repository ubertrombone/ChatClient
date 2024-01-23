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
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import settings.SettingsRepository
import util.Status
import util.Username

// TODO: Consider statuses that are because of no internet or server issues; how to handle that to avoid
//  data getting out of sync?
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

    // TODO: Apply the new helper functions to authenticated routes and check that results are as expected on routes that have already been implemented.
    // TODO: Add logout callback to any component that calls an authenticated route
    override suspend fun register(account: AccountRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/register", body = account) {
            if (status == HttpStatusCode.OK) Status.Success.also {
                settings.apply {
                    token.set(bodyAsText())
                    username.set(account.username.name)
                    password.set(account.password) // TODO: Encrypt password in local storage
                }
            } else Status.Error(bodyAsText())
        }
    }

    @Authenticated
    override suspend fun login() =
        withContext(scope.coroutineContext) { getHelper("/login") { authenticatedResponseHelper() } }

    override suspend fun authenticate(credentials: AuthenticationRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/authenticate", body = credentials) {
            when (status) {
                HttpStatusCode.OK -> Status.Success.also { settings.token.set(bodyAsText()) }
                HttpStatusCode.BadRequest -> Status.Error(bodyAsText())
                else -> Status.Error("Unknown error - the server may be down. Try logging in again later.")
            }
        }
    }

    @Authenticated
    override suspend fun logout() =
        withContext(scope.coroutineContext) { getHelper("/logout") { authenticatedResponseHelper() } }

    @Authenticated
    override suspend fun queryUsers(query: String): Set<Username>? = withContext(scope.coroutineContext) {
        postReturnHelper<String, Set<Username>?>(route = "/users", body = query) {
            nullableAuthenticatedResponseHelper<Set<Username>>()
        }
    }

    @Authenticated
    override suspend fun getFriends() = withContext(scope.coroutineContext) {
        getHelper("/friends") { nullableAuthenticatedResponseHelper<Set<FriendInfo>>() }
    }

    @Authenticated
    override suspend fun add(friend: Username) = withContext(scope.coroutineContext) {
        postHelper(route = "/friends/add", body = friend) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun remove(friend: Username) = withContext(scope.coroutineContext) {
        postHelper(route = "/friends/remove", body = friend) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun getSentFriendRequests() = withContext(scope.coroutineContext) {
        getHelper("/friend_request/sent_friend_requests") { nullableAuthenticatedResponseHelper<Set<FriendRequest>>() }
    }

    @Authenticated
    override suspend fun getReceivedFriendRequests() = withContext(scope.coroutineContext) {
        getHelper("/friend_request/received_friend_requests") { nullableAuthenticatedResponseHelper<Set<FriendRequest>>() }
    }

    @Authenticated
    override suspend fun sendFriendRequest(to: Username) = withContext(scope.coroutineContext) {
        postHelper(route = "/friend_request", body = to) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun addRequest(friendRequest: FriendRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/friend_request/add", body = friendRequest) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun cancelFriendRequest(to: Username) = withContext(scope.coroutineContext) {
        postHelper(route = "/friend_request/cancel_request", body = to) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun removeFriendRequest(request: FriendRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/friend_request/close_request", body = request) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun getBlockList() = withContext(scope.coroutineContext) {
        getHelper("/block") { nullableAuthenticatedResponseHelper<Set<Username>>() }
    }

    @Authenticated
    override suspend fun block(user: Username) =
        withContext(scope.coroutineContext) { postHelper(route = "/block", body = user) { authenticatedResponseHelper() } }

    @Authenticated
    override suspend fun unblock(user: Username) = withContext(scope.coroutineContext) {
        postHelper(route = "/block/unblock", body = user) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun deleteChat(id: Int) = withContext(scope.coroutineContext) {
        postHelper(route = "/chat_actions/delete_chat", body = id) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun deleteMessage(id: Int) = withContext(scope.coroutineContext) {
        postHelper(route = "/chat_actions/delete_message", body = id) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun getGroupChats() = withContext(scope.coroutineContext) {
        getHelper("group_chat") { nullableAuthenticatedResponseHelper<Set<GroupChat>>() }
    }

    @Authenticated
    override suspend fun createGroupChat(name: GroupChatNameRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/group_chat", body = name) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun getStatus() = withContext(scope.coroutineContext) {
        getHelper("/status") { nullableAuthenticatedResponseHelper<String?>() }
    }

    @Authenticated
    override suspend fun update(status: StatusRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/status", body = status) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun getCache() = withContext(scope.coroutineContext) {
        getHelper("/settings/cache") { nullableAuthenticatedResponseHelper<Boolean>() }
    }

    @Authenticated
    override suspend fun update(cache: Boolean) = withContext(scope.coroutineContext) {
        postHelper(route = "/settings/cache", body = cache) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun update(password: UpdatePasswordRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/settings/updatepwd", body = password) {
            authenticatedResponseHelper().also { if (status == HttpStatusCode.OK) settings.password.set(password.newPassword) }
        }
    }

    @Authenticated
    override suspend fun update(username: UpdateUsernameRequest) = withContext(scope.coroutineContext) {
        postHelper(route = "/settings/updateuser", body = username) { authenticatedResponseHelper() }
    }

    @Authenticated
    override suspend fun deleteAccount(decision: Boolean) = withContext(scope.coroutineContext) {
        postHelper(route = "/settings/delete", body = decision) { authenticatedResponseHelper() }
    }

    private suspend inline fun <reified T> postHelper(
        route: String,
        body: T,
        crossinline operation: suspend HttpResponse.() -> Status
    ): Status = withContext(scope.coroutineContext) {
        client.post(route) {
            contentType(ContentType.Application.Json)
            setBody(body)
            bearerAuth(settings.token.get())
        }.let { operation(it) }
    }

    private suspend inline fun <reified T, R> postReturnHelper(
        route: String,
        body: T,
        crossinline operation: suspend HttpResponse.() -> R
    ): R = withContext(scope.coroutineContext) {
        client.post(route) {
            contentType(ContentType.Application.Json)
            setBody(body)
            bearerAuth(settings.token.get())
        }.let { operation(it) }
    }

    private suspend inline fun <T> getHelper(route: String, crossinline operation: suspend HttpResponse.() -> T): T =
        withContext(scope.coroutineContext) {
            with(client.get(route) { bearerAuth(settings.token.get()) }) { operation(this) }
        }

    private suspend fun HttpResponse.authenticatedResponseHelper(): Status =
        withContext(scope.coroutineContext) { if (status == HttpStatusCode.OK) Status.Success else Status.Error(this@authenticatedResponseHelper) }

    // TODO: Any get function will need to check Error based on string instead of HttpResponse
    private suspend inline fun <reified T> HttpResponse.nullableAuthenticatedResponseHelper(): T? =
        withContext(scope.coroutineContext) {
            when (status) {
                HttpStatusCode.OK -> body<T>()
                HttpStatusCode.Unauthorized -> throw Exception(status.description)
                else -> null
            }
        }

    override fun onDestroy() {
        scope.cancel()
        client.close()
    }
}