package core.impl

import core.interfaces.NetworkRequests
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class NetworkRequests(
    private val baseUrl: String = ""
) : NetworkRequests {

    private val okHttpClient = OkHttpClient()

    override fun executeRequest(path: String, headers: Headers?): Response {
        return okHttpClient.newCall(createRequest(path, headers)).execute()
    }

    private fun createRequest(endpoint: String, headers: Headers?): Request {
        return Request.Builder().apply {
            url(baseUrl + endpoint)
            if (headers != null) headers(headers)
        }.build()
    }

}