package util

object Constants {
    const val PASSWORD_REQUIREMENT_MIN = 12
    const val REQUIREMENT_MAX = 24
    const val USERNAME_TOO_LONG = "Username must be no more than $REQUIREMENT_MAX characters!"
    const val USERNAME_TOO_SHORT = "Username must contain at least 1 character!"
    const val INVALID_CHARS_USERNAME = "Username can only contain alpha-numeric characters!"
    const val PASSWORD_REQUIRES_CHARS = "Password must contain at least 1 letter and 1 number."
    const val INVALID_CHARS_PASSWORD = "Invalid characters!"
    const val PASSWORD_SHORT = "Password must be at least $PASSWORD_REQUIREMENT_MIN characters!"
    const val PASSWORD_LONG = "Password must be no more than $REQUIREMENT_MAX characters!"
}