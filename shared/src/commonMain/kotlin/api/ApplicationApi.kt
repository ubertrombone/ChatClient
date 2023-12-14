package api

import api.model.*
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import settings.SettingsRepository
import util.Username

interface ApplicationApi {
    val settings: SettingsRepository
    val scope: CoroutineScope
    val client: HttpClient

    suspend fun register(account: AccountRequest)
    suspend fun login(account: LoginRequest)
    suspend fun logout()

    suspend fun getFriends()
    suspend fun add(friend: Username)
    suspend fun remove(friend: Username)

    suspend fun getSentFriendRequests()
    suspend fun getReceivedFriendRequests()
    suspend fun sendFriendRequest(to: Username)
    suspend fun cancelFriendRequest(to: Username) // TODO: This should be reworked on server side

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