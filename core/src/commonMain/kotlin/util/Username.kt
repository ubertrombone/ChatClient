package util

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Username(val name: String) {
    init {
        require(name.length >= Constants.USERNAME_REQUIREMENT_MIN) { Constants.USERNAME_TOO_SHORT }
        require(name.length <= Constants.REQUIREMENT_MAX) { "${Constants.USERNAME_TOO_LONG} Length is: ${name.length}" }
        require(name.none { !it.isLetterOrDigit() }) { Constants.INVALID_CHARS_USERNAME }
    }
}