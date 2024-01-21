package db

import app.cash.sqldelight.db.SqlDriver
import com.ubertrombone.chat.ChatDatabase

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DriverFactory() {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): ChatDatabase = ChatDatabase(driverFactory.createDriver())