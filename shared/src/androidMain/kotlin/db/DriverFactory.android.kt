package db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ubertrombone.chat.ChatDatabase
import com.ubertrombone.chatclient.ChatClientApp

actual class DriverFactory {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(ChatDatabase.Schema, ChatClientApp.appContext, "chat.db")
}