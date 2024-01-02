package util

import kotlinx.serialization.Serializable
import util.Constants.INVALID_CHARS_USERNAME
import util.Constants.REQUIREMENT_MAX
import util.Constants.UNKNOWN_ERROR
import util.Constants.USERNAME_REQUIREMENT_MIN
import util.Constants.USERNAME_TOO_LONG
import util.Constants.USERNAME_TOO_SHORT
import util.Status.Error
import util.Status.Success
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Username(val name: String) {
    init {
        require(name.length >= USERNAME_REQUIREMENT_MIN) { USERNAME_TOO_SHORT }
        require(name.length <= REQUIREMENT_MAX) { "$USERNAME_TOO_LONG Length is: ${name.length}" }
        require(name.none { !it.isLetterOrDigit() }) { INVALID_CHARS_USERNAME }
    }
}

fun String.toUsername() = Username(this)
fun String.toUsernameOrNull() = runCatching { toUsername() }.getOrNull()
fun String.usernameToStatus(): Status =
    runCatching { toUsername().let { Success } }.getOrElse { Error(it.message ?: UNKNOWN_ERROR) }