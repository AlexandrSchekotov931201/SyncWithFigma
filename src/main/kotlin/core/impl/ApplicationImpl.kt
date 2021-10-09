package core.impl

import Config.EXTENSION_SVG
import Config.PAGE_ICONS
import Config.PATH_RESOURCES_COLOR
import Config.PATH_RESOURCES_SVG
import Config.PATH_RESOURCES_VECTOR
import Config.STYLE_TYPE
import core.interfaces.Application
import core.interfaces.FigmaApi
import core.interfaces.FileManager
import core.interfaces.FileXmlManager
import core.model.Color
import enums.TypeCleaning
import utils.converters.SvgToVectorConvertor
import utils.ex.DELIMITER_COMMA
import utils.ex.appendPrefix
import utils.ex.extractNameOnly
import utils.ex.toSnakeCase

class ApplicationImpl(
    private val figmaApi: FigmaApi,
    private val fileManager: FileManager,
    private val fileXmlManager: FileXmlManager,
    private val svgToVectorConvertor: SvgToVectorConvertor
) : Application {

    override fun fetchIcons() {
        figmaApi.getFileComponents()?.apply {
            val idsBuilder = StringBuilder()
            val mappingIds: MutableMap<String, String> = mutableMapOf()
            val listThread: MutableList<Thread> = mutableListOf()

            fileManager.clearDirectory(PATH_RESOURCES_VECTOR, TypeCleaning.FILES_ONLY)
            fileManager.clearDirectory(PATH_RESOURCES_SVG, TypeCleaning.ALL_DIRECTORY)
            fileManager.createDirectory(PATH_RESOURCES_SVG)

            meta.components
                .filter { it.containingFrame.pageName == PAGE_ICONS }
                .filterNot { it.description.contains(TAG_IGNORE) }
                .filterNot { it.description.contains(TAG_IOS) }
                .forEach { component ->
                    mappingIds[component.nodeId] = component.name
                        .extractNameOnly()
                        .toSnakeCase()
                        .appendPrefix(PREFIX_FOR_COMPONENT_NAME)
                    idsBuilder.apply {
                        append(component.nodeId)
                        append(DELIMITER_COMMA)
                    }
                }

            figmaApi.getImages(idsBuilder.removeSuffix(DELIMITER_COMMA).toString())?.let { imageEntity ->
                imageEntity.images?.forEach { image ->
                    mappingIds[image.key]?.let { name ->
                        //TODO точно ли подходит подобное решение для быстрой загрузки картинок (Через Thread)?
                        // Утоничть как сделанно на IOS
                        val thread = Thread {
                            fileManager.uploadFile(image.value, name, EXTENSION_SVG)
                        }
                        thread.start()
                        listThread.add(thread)
                    }
                }
                listThread.forEach { it.join() }
            }
            svgToVectorConvertor.process()
            fileManager.clearDirectory(PATH_RESOURCES_SVG, TypeCleaning.ALL_DIRECTORY)
        }
    }

    override fun fetchColors() {
        figmaApi.getFileStyles()?.apply {
            val idsBuilder = StringBuilder()
            val mappingIds: MutableMap<String, String> = mutableMapOf()
            val allColors: MutableList<Color> = mutableListOf()

            fileManager.clearDirectory(PATH_RESOURCES_COLOR, TypeCleaning.ALL_DIRECTORY)
            fileManager.createDirectory(PATH_RESOURCES_COLOR)

            meta.styles
                .filter { it.styleType == STYLE_TYPE }
                .filterNot { it.description.contains(TAG_IGNORE) }
                .filterNot { it.description.contains("iOS") }
                .forEach { styles ->
                    mappingIds[styles.nodeId] = styles.name
                        .extractNameOnly()
                        .toSnakeCase()
                        .appendPrefix(PREFIX_FOR_COLOR_NAME)
                    idsBuilder.apply {
                        append(styles.nodeId)
                        append(DELIMITER_COMMA)
                    }
                }

            figmaApi.getFileNodes(idsBuilder.removeSuffix(DELIMITER_COMMA).toString())?.let { fileNodes ->
                fileNodes.nodes.forEach { node ->
                    mappingIds[node.key]?.let { name ->
                        val fill = node.value.document.fills.first()
                        val rgbColor = node.value.document.fills.first().color
                        val alpha = fill.opacity ?: rgbColor.a //TODO Необходимо учитывать opacity или alpha
                        val processedColor = Color(name, rgbColor.r, rgbColor.g, rgbColor.b, alpha)
                        allColors.add(processedColor)
                        println(name)
                    }
                }
            }

            fileXmlManager.createXmlFileFromColor("color", allColors.toList())
        }
    }

    override fun fetchFonts() {

    }

    private companion object {
        const val PREFIX_FOR_COMPONENT_NAME = "ic_"
        const val PREFIX_FOR_COLOR_NAME = "ds_"
        const val TAG_IGNORE = "[ignore]"
        const val TAG_IOS = "[ios]"
    }

}