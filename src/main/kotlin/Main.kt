import Config.PATH_RESOURCES_COLOR
import Config.PATH_RESOURCES_SVG
import Config.PATH_RESOURCES_VECTOR
import core.impl.FigmaApiImpl
import core.impl.ApplicationImpl
import core.impl.FileManagerImpl
import core.impl.FileXmlManagerImpl
import core.interfaces.FigmaApi
import utils.converters.SvgToVectorConvertor

val figmaApi: FigmaApi = FigmaApiImpl()
val fileManagerImpl = FileManagerImpl(PATH_RESOURCES_SVG)
val fileXmlManagerImpl = FileXmlManagerImpl(PATH_RESOURCES_COLOR)
val svgToVectorConvertor = SvgToVectorConvertor(PATH_RESOURCES_SVG, PATH_RESOURCES_VECTOR)
val recipientFromFigma = ApplicationImpl(
    figmaApi,
    fileManagerImpl,
    fileXmlManagerImpl,
    svgToVectorConvertor
)

fun main() {
    recipientFromFigma.apply {
        fetchIcons()
        fetchColors()
        fetchFonts()
    }
}