package core.feature.colorFetch

import Config.EXTENSION_XML
import Config.PATH_RESOURCES_DARK_COLOR
import Config.PATH_RESOURCES_LIGHT_COLOR
import Config.FILL_STYLE_TYPE
import Config.TAG_IGNORE
import core.interfaces.Fetcher
import core.api.FigmaApi
import core.enums.Theme
import core.interfaces.FileManager
import core.feature.colorFetch.model.Color
import utils.ColorsHandler
import utils.converters.ColorToXmlConvertor
import utils.ex.*

class ColorFetcher(
    private val figmaApi: FigmaApi,
    private val fileManager: FileManager
) : Fetcher {

    override fun fetch() {
        val idsStyles = getIdsStyles()
        val colors: List<Color> = getColorsByIdsStyles(idsStyles)
        fileManager.deleteFiles(PATH_RESOURCES_LIGHT_COLOR, "$FILE_NAME$EXTENSION_XML")
        fileManager.deleteFiles(PATH_RESOURCES_DARK_COLOR, "$FILE_NAME$EXTENSION_XML")
        createColorFile(colors, Theme.LIGHT, PATH_RESOURCES_LIGHT_COLOR)
        createColorFile(colors, Theme.DARK, PATH_RESOURCES_DARK_COLOR)
    }

    private fun getIdsStyles(): String {
        val idsBuilder = StringBuilder()
        figmaApi.getFileStyles()?.apply {
            meta.styles
                .filter { it.styleType == FILL_STYLE_TYPE }
                .filterNot { it.description.contains(TAG_IGNORE) }
                .filterNot { it.description.contains(IOS) }
                .forEach { styles ->
                    idsBuilder.apply {
                        append(styles.nodeId)
                        append(DELIMITER_COMMA)
                    }
                }
        }
        return idsBuilder.removeSuffix(DELIMITER_COMMA).toString()
    }

    private fun getColorsByIdsStyles(idsStyles: String): List<Color> {
        val colors: MutableList<Color> = mutableListOf()
        figmaApi.getFileNodes(idsStyles)?.let { fileNodes ->
            fileNodes.nodes.forEach { node ->
                val nameColor = node.value.document.name.replace(spaceRegex, EMPTY_STRING)
                val fillColor = node.value.document.fills.first()
                val rgbColor = node.value.document.fills.first().color
                val alphaColor = fillColor.opacity ?: rgbColor.a //TODO Необходимо учитывать opacity или alpha
                colors.add(Color(nameColor, rgbColor.r, rgbColor.g, rgbColor.b, alphaColor))
            }
        }
        return colors.toList()
    }

    private fun createColorFile(colors: List<Color>, theme: Theme, pathForSave: String) {
        val processedColors = ColorsHandler.handle(colors, theme)
        val convertedColorsToXml = ColorToXmlConvertor.convertColorsToXml(processedColors)
        fileManager.createFile(
            convertedColorsToXml,
            FILE_NAME,
            EXTENSION_XML,
            pathForSave
        )
    }

    private companion object {
        const val FILE_NAME = "ds_colors"
        const val IOS = "iOS"
    }
}