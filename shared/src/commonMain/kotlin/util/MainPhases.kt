package util

import kotlinx.serialization.Serializable

@Serializable
enum class MainPhases {
    LOGIN,
    REGISTER,
    MAIN
}