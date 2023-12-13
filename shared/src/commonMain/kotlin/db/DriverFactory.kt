package db

import app.cash.sqldelight.db.SqlDriver
import com.joshrose.chat.ChatDatabase

expect class DriverFactory() {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): ChatDatabase = ChatDatabase(driverFactory.createDriver())