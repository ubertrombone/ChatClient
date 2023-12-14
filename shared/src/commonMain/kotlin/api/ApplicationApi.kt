package api

import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope

interface ApplicationApi {
    val token: String
    val scope: CoroutineScope
    val client: HttpClient

    
}