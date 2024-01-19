package util

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Password(private val pass: String) {
    init {
        val invalidChars = listOf(' ', '\\', '`', '#')
        require(pass.contains(Regex("^(?=.*[0-9])"))) { Constants.PASSWORD_REQUIRES_CHARS }
        require(pass.contains(Regex("^(?=.*[a-zA-Z])"))) { Constants.PASSWORD_REQUIRES_CHARS }
        require(pass.none { invalidChars.contains(it) }) { Constants.INVALID_CHARS_PASSWORD }
        require(pass.length >= Constants.PASSWORD_REQUIREMENT_MIN) { "${Constants.PASSWORD_SHORT} Length is: ${pass.length}" }
        require(pass.length <= Constants.REQUIREMENT_MAX) { "${Constants.PASSWORD_LONG} Length is: ${pass.length}" }
    }
}

fun String.toPassword() = Password(this)
fun String.toPasswordOrNull() = runCatching { toPassword() }.getOrNull()
fun String.passwordToStatus(): Status =
    runCatching { toPassword().let { Status.Success } }.getOrElse {
        Status.Error(
            it.message ?: Constants.UNKNOWN_ERROR
        )
    }