package db

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.joshrose.chat.ChatDatabase
import com.joshrose.chat.Chats
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chats: ChatDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    private val scope = CoroutineScope(dispatcher + SupervisorJob())

    fun getChats(): Flow<List<Chats>> = chats.chatsQueries.selectAll().asFlow().mapToList(dispatcher)
}