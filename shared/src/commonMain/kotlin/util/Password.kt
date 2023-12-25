package util

import kotlinx.serialization.Serializable
import util.Constants.INVALID_CHARS_PASSWORD
import util.Constants.PASSWORD_LONG
import util.Constants.PASSWORD_REQUIREMENT_MIN
import util.Constants.PASSWORD_REQUIRES_CHARS
import util.Constants.PASSWORD_SHORT
import util.Constants.REQUIREMENT_MAX
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Password(val pass: String) {
    init {
        val invalidChars = listOf(' ', '\\', '`', '#')
        require(pass.contains(Regex("^(?=.*[0-9])"))) { PASSWORD_REQUIRES_CHARS }
        require(pass.contains(Regex("^(?=.*[a-zA-Z])"))) { PASSWORD_REQUIRES_CHARS }
        require(pass.none { invalidChars.contains(it) }) { INVALID_CHARS_PASSWORD }
        require(pass.length >= PASSWORD_REQUIREMENT_MIN) { PASSWORD_SHORT }
        require(pass.length <= REQUIREMENT_MAX) { PASSWORD_LONG }
    }
}

fun String.toPassword() = Password(this)