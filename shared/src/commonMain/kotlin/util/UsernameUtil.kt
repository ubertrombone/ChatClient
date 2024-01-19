package util

import util.Constants.UNKNOWN_ERROR
import util.Status.Error
import util.Status.Success

fun String.toUsername() = Username(this)
fun String.toUsernameOrNull() = runCatching { toUsername() }.getOrNull()
fun String.usernameToStatus(): Status =
    runCatching { toUsername().let { Success } }.getOrElse { Error(it.message ?: UNKNOWN_ERROR) }