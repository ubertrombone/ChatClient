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
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
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
            client.post("/register") {
                contentType(ContentType.Application.Json)
                setBody(account)
            }.let { response ->
                if (response.status == Conflict) Error(response.bodyAsText()).also(status)
                else {
                    val body = response.body<SimpleResponse>()
                    if (body.successful) {
                        settings.apply {
                            token.set(body.message)
                            username.set(account.username.name)
                            password.set(account.password) // TODO: Encrypt password in local storage
                        }
                        Success.also(status)
                    } else Error(body.message).also(status)
                }
            }
        } catch (e: Exception) {
            Error(e.message ?: "Exception called in ApplicationApiImpl.register").also {
                status(it)
                Napier.e(message = it.message, throwable = e)
            }
        }
    }

    override suspend fun login(status: (Status) -> Unit) = withContext(scope.coroutineContext) {
        status(Loading)

        return@withContext try {
            when (client.get("/login").status) {
                OK -> Success.also(status)
                Unauthorized -> authenticate(
                    credentials = AuthenticationRequest(
                        username = settings.username.get().toUsername(),
                        password = settings.password.get()
                    ),
                    status = status
                )
                else -> Error("Unknown error - the server may be down. Try logging in again later.")
            }
        } catch (e: Exception) {
            Error(e.message ?: "Exception called in ApplicationApiImpl.login").also {
                status(it)
                Napier.e(message = it.message, throwable = e)
            }
        }
    }

    override suspend fun authenticate(credentials: AuthenticationRequest, status: (Status) -> Unit) = withContext(scope.coroutineContext) {
        status(Loading)

        return@withContext try {
            client.post("/authenticate") {
                contentType(ContentType.Application.Json)
                setBody(credentials)
            }.let { response ->
                when (response.status) {
                    OK -> {
                        settings.token.set(response.bodyAsText())
                        Success.also(status)
                    }
                    BadRequest -> Error(response.bodyAsText()).also(status)
                    else -> Error("Unknown error - the server may be down. Try logging in again later.")
                }
            }
        } catch (e: Exception) {
            Error(e.message ?: "Exception called in ApplicationApiImpl.login").also {
                status(it)
                Napier.e(message = it.message, throwable = e)
            }
        }
    }

    override suspend fun logout(status: (Status) -> Unit) = withContext(scope.coroutineContext) {
        status(Loading)
        return@withContext try {
            val post = client.get("/logout")
            if (post.status == OK) Success.also(status) else Error("There was a problem logging out!").also(status)
        } catch (e: Exception) {
            Error(e.message ?: "Exception called in ApplicationApiImpl.logout").also {
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