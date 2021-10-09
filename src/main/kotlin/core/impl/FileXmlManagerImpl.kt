package core.impl

import Config.EXTENSION_XML
import core.interfaces.FileXmlManager
import core.model.Color
import org.xembly.Directives
import org.xembly.Xembler
import utils.ex.EMPTY_STRING

import java.io.FileOutputStream


class FileXmlManagerImpl(
    private val destination: String = ""
) : FileXmlManager {

    override fun createXmlFileFromColor(fileName: String, colors: List<Color>) {
        val fileOutputStream = FileOutputStream("$destination/$fileName$EXTENSION_XML")
        val createdXmlFromColors = createXmlFromColors(colors)
        val formattedXmlFromColors = formatXml(createdXmlFromColors)
        val buffer = formattedXmlFromColors.toByteArray()
        fileOutputStream.write(buffer)
        fileOutputStream.close()
    }

    private fun createXmlFromColors(colors: List<Color>): String {
        val directives = Directives().add(ROOT_ELEMENT_NAME)
        val xembler = Xembler(directives)
        colors.forEach { color ->
            directives
                .add(CHILD_ELEMENT_NAME)
                .attr(ATTRIBUTE_NAME, color.name)
                .set(color.hex)
                .up()
        }
        return xembler.xml()
    }

    private fun formatXml(xml: String): String {
        return xml.replace(OLD_VALUE_STANDALONE, EMPTY_STRING)
            .replace(OLD_VALUE_RESOURCES_START, NEW_VALUE_RESOURCES_START)
            .replace(OLD_VALUE_COLOR, NEW_VALUE_COLOR)
            .replace(OLD_VALUE_RESOURCES_END, NEW_VALUE_RESOURCES_END)
    }

    private companion object {
        const val ROOT_ELEMENT_NAME = "resources"
        const val CHILD_ELEMENT_NAME = "color"
        const val ATTRIBUTE_NAME = "name"

        const val OLD_VALUE_RESOURCES_START = "<resources>\r\n"
        const val NEW_VALUE_RESOURCES_START = "<resources>\n\t"

        const val OLD_VALUE_RESOURCES_END = "</color>\n\t</resources>"
        const val NEW_VALUE_RESOURCES_END = "</color>\n</resources>"

        const val OLD_VALUE_COLOR = "</color>\r\n"
        const val NEW_VALUE_COLOR = "</color>\n\t"
        const val OLD_VALUE_STANDALONE = "standalone=\"no\""
    }
}