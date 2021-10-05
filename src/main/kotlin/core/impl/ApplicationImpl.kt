package core.impl

import Config.SRC_RESOURCES_SVG
import Config.SRC_RESOURCES_VECTOR
import core.interfaces.Application
import enums.PageName
import core.interfaces.FigmaApi
import core.interfaces.FileManagement
import enums.PurificationDegree
import okhttp3.internal.notify
import utils.converters.SvgFilesProcessorKt
import utils.ex.appendPrefix
import utils.ex.extractNameOnly
import utils.ex.toSnakeCase

class ApplicationImpl(
    private val figmaApi: FigmaApi,
    private val fileManagement: FileManagement,
    private val svgFilesProcessorKt: SvgFilesProcessorKt
) : Application {

    override fun fetchIcons() {
        figmaApi.getFileComponents()?.apply {
            val idsBuilder = StringBuilder()
            val mappingIds: MutableMap<String, String> = mutableMapOf()
            val listThread: MutableList<Thread> = mutableListOf()

            fileManagement.clearDirectory(SRC_RESOURCES_VECTOR, PurificationDegree.FILES_ONLY)
            fileManagement.clearDirectory(SRC_RESOURCES_SVG, PurificationDegree.WHOLE_DIRECTORY)
            fileManagement.createDirectory(SRC_RESOURCES_SVG)

            meta.components
                .filter { it.containingFrame.pageName == PageName.ICONS.pageName }
                .filter { it.description != TAG_IGNORE }
                .filter { it.description != TAG_IOS }
                .forEachIndexed { index, component ->
                    mappingIds[component.nodeId] = component.name
                        .extractNameOnly()
                        .toSnakeCase()
                        .appendPrefix(PREFIX_FOR_COMPONENT_NAME)
                    idsBuilder.apply {
                        append(component.nodeId)
                        append(DELIMITER_FOR_IDS)
                    }
                }

            figmaApi.getImages(idsBuilder.removeSuffix(DELIMITER_FOR_IDS).toString())?.let { imageEntity ->
                imageEntity.images?.forEach { image ->
                    mappingIds[image.key]?.let { name ->
                        //TODO точно ли подходит подобное решение для быстрой загрузки картинок (Через Thread)?
                        // Утоничть как сделанно на IOS
                        val thread = Thread {
                            fileManagement.uploadFile(image.value, name, EXTENSION_FOR_UPLOAD_FILE)
                        }
                        thread.start()
                        listThread.add(thread)
                    }
                }
                listThread.forEach { it.join() }
            }
            svgFilesProcessorKt.process()
            fileManagement.clearDirectory(SRC_RESOURCES_SVG, PurificationDegree.WHOLE_DIRECTORY)
        }
    }

    private companion object {
        const val EXTENSION_FOR_UPLOAD_FILE = ".svg"
        const val PREFIX_FOR_COMPONENT_NAME = "ic_"
        const val DELIMITER_FOR_IDS = ","
        const val TAG_IGNORE = "[ignore]"
        const val TAG_IOS = "[ios]"
    }

}