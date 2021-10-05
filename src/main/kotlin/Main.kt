import Config.SRC_RESOURCES_SVG
import Config.SRC_RESOURCES_VECTOR
import core.impl.FigmaApiImpl
import core.impl.ApplicationImpl
import core.impl.FileManagementImpl
import core.interfaces.FigmaApi
import utils.converters.SvgFilesProcessorKt

val figmaApi: FigmaApi = FigmaApiImpl()
val uploadingFiles = FileManagementImpl(SRC_RESOURCES_SVG)
val svgFilesProcessorKt = SvgFilesProcessorKt(SRC_RESOURCES_SVG, SRC_RESOURCES_VECTOR)
val recipientFromFigmaImpl = ApplicationImpl(
    figmaApi,
    uploadingFiles,
    svgFilesProcessorKt
)

fun main(args: Array<String>) {
    recipientFromFigmaImpl.fetchIcons()
}