package db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.ubertrombone.chat.ChatDatabase
import com.ubertrombone.chat.Chats
import com.ubertrombone.chat.Messages
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import model.Message

class ChatRepository(
    private val chats: ChatDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    fun getChats(): Flow<List<Chats>> = chats.chatsQueries.selectAll().asFlow().mapToList(dispatcher)
    fun getChatById(id: Int): Flow<Chats?> = chats.chatsQueries.selectChatByID(id.toLong()).asFlow().mapToOneOrNull(dispatcher)
    fun selectChat(userOne: String, userTwo: String): Flow<Chats?> =
        chats.chatsQueries.selectChat(userOne, userTwo).asFlow().mapToOneOrNull(dispatcher)
    suspend fun insertChat(userOne: String, userTwo: String) = withContext(scope.coroutineContext) {
        chats.chatsQueries.insertChat(userOne, userTwo)
    }
    suspend fun updateChat(userOne: String, userTwo: String, id: Int) = withContext(scope.coroutineContext) {
        chats.chatsQueries.updateChat(userOne = userOne, userTwo = userTwo, id = id.toLong())
    }
    suspend fun deleteChat(id: Int) = withContext(scope.coroutineContext) {
        chats.chatsQueries.deleteChat(id.toLong())
    }

    fun getMessages(): Flow<List<Messages>> = chats.messagesQueries.selectAll().asFlow().mapToList(dispatcher)
    fun getMessagesFromChat(chatId: Int): Flow<List<Messages>> =
        chats.messagesQueries.selectAllFromChat(chatId.toLong()).asFlow().mapToList(dispatcher)
    fun getMessage(id: Int): Flow<Messages?> =
        chats.messagesQueries.selectCache(id.toLong()).asFlow().mapToOneOrNull(dispatcher)
    suspend fun insertMessage(message: Message) = withContext(scope.coroutineContext) {
        chats.messagesQueries.insertCache(
            message = message.message,
            sender = message.sender,
            timestamp = message.timestamp.epochSeconds,
            primaryUserRef = message.primaryUserRef,
            error = message.error?.toLong(),
            chat = message.chat.toLong()
        )
    }
    suspend fun editMessage(message: Message) = withContext(scope.coroutineContext) {
        chats.messagesQueries.updateCache(
            id = message.id?.toLong() ?: throw NullPointerException("Cannot edit Message with null ID."),
            message = message.message,
            sender = message.sender,
            timestamp = message.timestamp.epochSeconds,
            primaryUserRef = message.primaryUserRef,
            error = message.error?.toLong(),
            chat = message.chat.toLong()
        )
    }
    suspend fun deleteMessage(id: Int) = withContext(scope.coroutineContext) {
        chats.messagesQueries.deleteCache(id.toLong())
    }
    suspend fun deleteAllMessages() = withContext(scope.coroutineContext) { chats.messagesQueries.deleteAll() }
}