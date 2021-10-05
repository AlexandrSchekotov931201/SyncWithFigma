package core.impl

import com.google.gson.Gson
import entities.FileComponentsEntity
import entities.ImagesEntity
import core.interfaces.FigmaApi
import okhttp3.Headers
import okhttp3.Response
import okhttp3.internal.format

class FigmaApiImpl : FigmaApi {

    private val network = NetworkRequests(BASE_URL)
    private val gson = Gson()

    private val defaultHeaders = Headers.Builder().add(
        "X-FIGMA-TOKEN", X_FIGMA_TOKEN
    ).build()

    override fun getImages(nodeIds: String): ImagesEntity? {
        executeRequestApi(format(GET_IMAGE_ENDPOINT, nodeIds))
            .body?.let { body ->
                val json = body.string()
                return gson.fromJson(json, ImagesEntity::class.java)
            }
        return null
    }

    override fun getFileComponents(): FileComponentsEntity? {
        executeRequestApi(GET_FILE_COMPONENTS_ENDPOINT)
            .body?.let { body ->
                val json = body.string()
                return gson.fromJson(json, FileComponentsEntity::class.java)
            }
        return null
    }

    private fun executeRequestApi(
        path: String,
        headers: Headers = defaultHeaders
    ): Response {
        return network.executeRequest(path, headers)
    }

    private companion object {
        const val BASE_URL = "https://api.figma.com"
        const val FILE_KEY = "Xmtnszd3c3ENk0KGTDcGb2"
        const val X_FIGMA_TOKEN = "218464-30234630-efed-492c-abc6-1234836376ec"

        const val GET_FILE_COMPONENTS_ENDPOINT = "/v1/files/$FILE_KEY/components"
        const val GET_IMAGE_ENDPOINT = "/v1/images/$FILE_KEY?ids=%s&format=svg&use_absolute_bounds=true"
    }

}