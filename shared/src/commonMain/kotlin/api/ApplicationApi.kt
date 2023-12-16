package api

import api.model.*
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import settings.SettingsRepository
import util.Status
import util.Username

interface ApplicationApi {
    val settings: SettingsRepository
    val scope: CoroutineScope
    val client: HttpClient

    suspend fun register(account: AccountRequest, status: (Status) -> Unit = {}): Status
    suspend fun login(status: (Status) -> Unit = {}): Status
    suspend fun authenticate(credentials: AuthenticationRequest, status: (Status) -> Unit = {}): Status
    suspend fun logout(status: (Status) -> Unit = {}): Status

    suspend fun getFriends()
    suspend fun add(friend: Username)
    suspend fun remove(friend: Username)

    suspend fun getSentFriendRequests()
    suspend fun getReceivedFriendRequests()
    suspend fun sendFriendRequest(to: Username)
    suspend fun cancelFriendRequest(to: Username)

    suspend fun getBlockList()
    suspend fun block(user: Username)
    suspend fun unblock(user: Username)

    suspend fun getGroupChats()
    suspend fun createGroupChat(name: GroupChatNameRequest)

    suspend fun getStatus()
    suspend fun update(status: StatusRequest)

    suspend fun update(password: UpdatePasswordRequest)
    suspend fun update(username: UpdateUsernameRequest)
    suspend fun deleteAccount(decision: Boolean)
}