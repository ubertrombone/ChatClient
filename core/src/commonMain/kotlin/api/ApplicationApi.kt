package api

import api.model.*
import util.Status
import util.Username

interface ApplicationApi {
    suspend fun register(account: AccountRequest): Status
    suspend fun login(): Status
    suspend fun authenticate(credentials: AuthenticationRequest): Status
    suspend fun logout(): Status

    suspend fun queryUsers(query: String): Set<Username>?

    suspend fun getFriends(): Set<FriendInfo>?
    suspend fun add(friend: Username): Status
    suspend fun remove(friend: Username): Status

    suspend fun getSentFriendRequests(): Set<FriendRequest>?
    suspend fun getReceivedFriendRequests(): Set<FriendRequest>?
    suspend fun sendFriendRequest(to: Username): Status
    suspend fun addRequest(friendRequest: FriendRequest): Status
    suspend fun cancelFriendRequest(to: Username): Status
    suspend fun removeFriendRequest(request: FriendRequest): Status

    suspend fun getBlockList(): Set<Username>?
    suspend fun block(user: Username): Status
    suspend fun unblock(user: Username): Status

    suspend fun deleteMessage(id: Int): Status

    suspend fun getGroupChats(): Set<GroupChat>?
    suspend fun createGroupChat(name: GroupChatNameRequest): Status

    suspend fun getStatus(): String?
    suspend fun update(status: StatusRequest): Status

    suspend fun getCache(): Boolean?
    suspend fun update(cache: Boolean): Status
    suspend fun update(password: UpdatePasswordRequest): Status
    suspend fun update(username: UpdateUsernameRequest): Status
    suspend fun deleteAccount(decision: Boolean): Status
}