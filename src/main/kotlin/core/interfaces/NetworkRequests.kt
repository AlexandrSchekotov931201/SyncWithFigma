package core.interfaces

import okhttp3.Headers
import okhttp3.Response

interface NetworkRequests {
    fun executeRequest(path: String, headers: Headers? = null): Response
}