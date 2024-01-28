package util

// TODO: Return to this and add ERROR property
enum class Functions {
    GROUP,
    INDIVIDUAL,
    LEAVE,
    JOIN,
    ERROR
}

//@Serializable
//sealed interface Functions {
//    @Serializable data object Group : Functions
//    @Serializable data object Individual : Functions
//    @Serializable data object Leave : Functions
//    @Serializable data object Join : Functions
//    @Serializable data class Error(val message: String) : Functions
//}