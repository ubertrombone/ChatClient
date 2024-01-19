package db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.ubertrombone.chat.ChatDatabase
import com.ubertrombone.chatclient.ChatClientApp

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(ChatDatabase.Schema, ChatClientApp.appContext, "chat.db")
}