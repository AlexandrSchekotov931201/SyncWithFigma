package core.impl

import com.google.gson.Gson
import entities.FileComponentsEntity
import entities.ImagesEntity
import core.interfaces.FigmaApi
import entities.GetFileNodesEntity
import entities.GetFileStylesEntity
import okhttp3.Headers
import okhttp3.Response
import okhttp3.internal.format

class FigmaApiImpl : FigmaApi {

    private val network = NetworkRequests(BASE_URL)
    private val gson = Gson()

    private val defaultHeaders = Headers.Builder().add(X_FIGMA_TOKEN_ARG, X_FIGMA_TOKEN).build()

    override fun getFileNodes(nodeIds: String): GetFileNodesEntity? {
        executeRequestApi(format(GET_FILE_NODES_ENDPOINT, nodeIds)).body?.let { body ->
            val json = body.string()
            return gson.fromJson(json, GetFileNodesEntity::class.java)
        }
        return null
    }

    override fun getImages(nodeIds: String): ImagesEntity? {
        executeRequestApi(format(GET_IMAGE_ENDPOINT, nodeIds)).body?.let { body ->
            val json = body.string()
            return gson.fromJson(json, ImagesEntity::class.java)
        }
        return null
    }

    override fun getFileComponents(): FileComponentsEntity? {
        executeRequestApi(GET_FILE_COMPONENTS_ENDPOINT).body?.let { body ->
            val json = body.string()
            return gson.fromJson(json, FileComponentsEntity::class.java)
        }
        return null
    }

    override fun getFileStyles(): GetFileStylesEntity? {
        executeRequestApi(GET_FILE_STYLES_ENDPOINT).body?.let { body ->
            val json = body.string()
            return gson.fromJson(json, GetFileStylesEntity::class.java)
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
        const val X_FIGMA_TOKEN = "249195-2f8db857-cdaa-47e3-a9c0-8720e9b03061"
        const val X_FIGMA_TOKEN_ARG = "X-FIGMA-TOKEN"

        const val GET_FILE_NODES_ENDPOINT = "/v1/files/$FILE_KEY/nodes?ids=%s"
        const val GET_FILE_COMPONENTS_ENDPOINT = "/v1/files/$FILE_KEY/components"
        const val GET_IMAGE_ENDPOINT = "/v1/images/$FILE_KEY?ids=%s&format=svg&use_absolute_bounds=true"
        const val GET_FILE_STYLES_ENDPOINT = "/v1/files/$FILE_KEY/styles"
    }

}