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
        // TODO: Account deletion should delete everything but the username which should be archived.
    }

    override fun onDestroy() {
        scope.cancel()
        client.close()
    }
}