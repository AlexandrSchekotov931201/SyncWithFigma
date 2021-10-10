import Config.PATH_RESOURCES_SVG
import Config.PATH_RESOURCES_VECTOR
import com.google.gson.Gson
import core.feature.colorFetch.ColorFetcher
import core.feature.iconFetch.IconFetcher
import core.impl.*
import core.api.FigmaApi
import core.api.FigmaApiImpl
import core.network.NetworkRequestsImpl
import okhttp3.OkHttpClient
import utils.converters.SvgToVectorConvertor


private val gson = Gson()
private val okHttpClient = OkHttpClient()
private val networkRequests = NetworkRequestsImpl(okHttpClient)
private val fileManagerImpl = FileManagerImpl(networkRequests)
private val figmaApi: FigmaApi = FigmaApiImpl(gson, networkRequests)
private val svgToVectorConvertor = SvgToVectorConvertor(PATH_RESOURCES_SVG, PATH_RESOURCES_VECTOR)

private val iconFetcher = IconFetcher(figmaApi, fileManagerImpl, svgToVectorConvertor)
private val colorFetcher = ColorFetcher(figmaApi, fileManagerImpl)

fun main() {
//    iconFetcher.fetch()
    colorFetcher.fetch()
}