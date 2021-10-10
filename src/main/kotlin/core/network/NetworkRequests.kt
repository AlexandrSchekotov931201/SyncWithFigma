package core.network

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

interface NetworkRequests {
    fun executeRequest(url: String, headers: Headers? = null): Response
}