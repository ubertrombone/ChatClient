package db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.joshrose.ChatClientApp
import com.joshrose.chat.ChatDatabase

actual class DriverFactory {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(ChatDatabase.Schema, ChatClientApp.appContext, "chat.db")
}