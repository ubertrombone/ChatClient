package db

import app.cash.sqldelight.db.SqlDriver
import com.ubertrombone.chat.ChatDatabase

expect class DriverFactory() {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): ChatDatabase = ChatDatabase(driverFactory.createDriver())