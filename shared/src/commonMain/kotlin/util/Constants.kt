package util

object Constants {
    const val USERNAME_REQUIREMENT_MIN = 5
    const val PASSWORD_REQUIREMENT_MIN = 12
    const val REQUIREMENT_MAX = 24
    const val USERNAME_TOO_LONG = "Username must be no more than $REQUIREMENT_MAX characters!"
    const val USERNAME_TOO_SHORT = "Username must contain at least $USERNAME_REQUIREMENT_MIN characters!"
    const val INVALID_CHARS_USERNAME = "Username can only contain alpha-numeric characters!"
    const val PASSWORD_REQUIRES_CHARS = "Password must contain at least 1 letter and 1 number."
    const val INVALID_CHARS_PASSWORD = "Invalid characters!"
    const val PASSWORD_SHORT = "Password must be at least $PASSWORD_REQUIREMENT_MIN characters!"
    const val PASSWORD_LONG = "Password must be no more than $REQUIREMENT_MAX characters!"
    const val INVALID_USERNAME = "Invalid Username"
    const val NO_PASSWORD_PROVIDED = "Password field is empty"
    const val PASSWORDS_NOT_MATCH = "Passwords don't match!"
    const val UNKNOWN_ERROR = "An unknown error occurred"
    const val MAX_STATUS_LENGTH = 256
    const val STATUS_TOO_LONG = "Status can't be more than $MAX_STATUS_LENGTH characters!"

    const val REQUESTS = "Requests"
    const val GROUP_CHATS = "Groups"
    const val FRIENDS = "Friends"
}