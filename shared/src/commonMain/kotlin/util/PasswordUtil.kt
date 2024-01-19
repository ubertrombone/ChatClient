package util

import util.Constants.UNKNOWN_ERROR
import util.Status.Error
import util.Status.Success

fun String.toPassword() = Password(this)
fun String.toPasswordOrNull() = runCatching { toPassword() }.getOrNull()
fun String.passwordToStatus(): Status =
    runCatching { toPassword().let { Success } }.getOrElse { Error(it.message ?: UNKNOWN_ERROR) }