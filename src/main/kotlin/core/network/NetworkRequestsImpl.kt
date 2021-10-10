package core.network

import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NetworkRequestsImpl(
    private val okHttpClient: OkHttpClient
) : NetworkRequests {

    override fun executeRequest(url: String, headers: Headers?): Response {
        return okHttpClient.newCall(createRequest(url, headers)).execute()
    }

    private fun createRequest(url: String, headers: Headers?): Request {
        return Request.Builder().apply {
            url(url)
            if (headers != null) headers(headers)
        }.build()
    }

}