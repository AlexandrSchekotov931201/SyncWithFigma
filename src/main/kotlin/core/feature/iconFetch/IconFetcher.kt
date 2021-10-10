package core.feature.iconFetch

import Config.EXTENSION_SVG
import Config.PAGE_ICONS
import Config.PATH_RESOURCES_SVG
import Config.PATH_RESOURCES_VECTOR
import Config.TAG_IGNORE
import Config.TAG_IOS
import core.enums.TypeCleaning
import core.interfaces.Fetcher
import core.api.FigmaApi
import core.interfaces.FileManager
import utils.converters.SvgToVectorConvertor
import utils.ex.DELIMITER_COMMA
import utils.ex.appendPrefix
import utils.ex.extractNameOnly
import utils.ex.toSnakeCase

class IconFetcher(
    private val figmaApi: FigmaApi,
    private val fileManager: FileManager,
    private val svgToVectorConvertor: SvgToVectorConvertor
): Fetcher {
    override fun fetch() {
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
                            fileManager.uploadFile(image.value, name, EXTENSION_SVG, PATH_RESOURCES_SVG)
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

    private companion object {
        const val PREFIX_FOR_COMPONENT_NAME = "ic_"
    }
}