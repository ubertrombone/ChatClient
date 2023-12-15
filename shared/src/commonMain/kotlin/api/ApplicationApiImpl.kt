package api

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
import io.ktor.http.HttpStatusCode.Companion.OK
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

class ApplicationApiImpl(override val settings: SettingsRepository) : InstanceKeeper.Instance, ApplicationApi {
    override val scope = CoroutineScope(Dispatchers.Main)
    override val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { prettyPrint = true })
        }

        install(Auth) {
            bearer {
                loadTokens { BearerTokens(settings.token.get(), "") }
            }
        }

        defaultRequest {
            url("http://0.0.0.0:8080")
        }
    }

    override suspend fun register(account: AccountRequest, status: (Status) -> Unit) = withContext(scope.coroutineContext) {
        // TODO: Username format validation on client side - Username uniqueness on server side
        // TODO: Password format validation on client side
        status(Loading)

        return@withContext try {
            val post = client.post("/register") {
                contentType(ContentType.Application.Json)
                setBody(account)
            }.body<SimpleResponse>()
            if (post.successful) {
                settings.apply {
                    token.set(post.message)
                    username.set(account.username.name)
                    password.set(account.password) // TODO: Encrypt password in local storage
                }
                Success.also(status)
            } else Error(post.message).also(status)
        } catch (e: Exception) {
            Error(e.message ?: "Exception called in ApplicationApiImpl.register").also {
                status(it)
                Napier.e(message = it.message, throwable = e)
            }
        }
    }
    override suspend fun login(account: LoginRequest) {
        // TODO: send just token to login route,
        //  If token is valid, response from server should return simple OK status
        //  If not valid, response should return Unauthorized
        //      this function will then call a new "reAuth" route with the user's user/pass from settings
        //      the server will return a new token
        //  Login route should also have the possibility to simply login with creds and not a token
        TODO("Not yet implemented")
    }
    override suspend fun logout(status: (Status) -> Unit) = withContext(scope.coroutineContext) {
        status(Loading)
        return@withContext try {
            val post = client.post("login/logout")
            if (post.status == OK) Success.also(status) else Error("There was a problem logging out!").also(status)
        } catch (e: Exception) {
            Error(e.message ?: "Exception called in ApplicationApiImpl.register").also {
                status(it)
                Napier.e(message = it.message, throwable = e)
            }
        }
    }

    override suspend fun getFriends() {
        TODO("Not yet implemented")
    }
    override suspend fun add(friend: Username) {
        TODO("Not yet implemented")
    }
    override suspend fun remove(friend: Username) {
        TODO("Not yet implemented")
    }

    override suspend fun getSentFriendRequests() {
        TODO("Not yet implemented")
    }
    override suspend fun getReceivedFriendRequests() {
        TODO("Not yet implemented")
    }
    override suspend fun sendFriendRequest(to: Username) {
        TODO("Not yet implemented")
    }
    override suspend fun cancelFriendRequest(to: Username) {
        TODO("Not yet implemented")
    }

    override suspend fun getBlockList() {
        TODO("Not yet implemented")
    }
    override suspend fun block(user: Username) {
        TODO("Not yet implemented")
    }
    override suspend fun unblock(user: Username) {
        TODO("Not yet implemented")
    }

    override suspend fun getGroupChats() {
        TODO("Not yet implemented")
    }
    override suspend fun createGroupChat(name: GroupChatNameRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun getStatus() {
        TODO("Not yet implemented")
    }
    override suspend fun update(status: StatusRequest) {
        TODO("Not yet implemented")
    }

    override suspend fun update(password: UpdatePasswordRequest) {
        TODO("Not yet implemented")
    }
    override suspend fun update(username: UpdateUsernameRequest) {
        TODO("Not yet implemented")
    }
    override suspend fun deleteAccount(decision: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        scope.cancel()
        client.close()
    }
}