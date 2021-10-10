package utils.converters

import core.feature.colorFetch.model.Color
import org.xembly.Directives
import org.xembly.Xembler
import utils.ex.EMPTY_STRING

object ColorToXmlConvertor {

    private const val ROOT_ELEMENT_NAME = "resources"
    private const val CHILD_ELEMENT_NAME = "color"
    private const val CHILD_ELEMENT_SPACE = "space"
    private const val ATTRIBUTE_NAME = "name"

    private const val OLD_VALUE_RESOURCES_START = "<resources>\r\n"
    private const val NEW_VALUE_RESOURCES_START = "<resources>\n\t"

    private const val OLD_VALUE_RESOURCES_END = "</color>\n\n\t</resources>"
    private const val NEW_VALUE_RESOURCES_END = "</color>\n</resources>"

    private const val OLD_VALUE_COLOR = "</color>\r\n"
    private const val NEW_VALUE_COLOR = "</color>\n\t"
    private const val OLD_VALUE_SPACE = "\t<space/>\r\n"

    private const val OLD_VALUE_STANDALONE = "standalone=\"no\""

    fun convertColorsToXml(colorsGroup: List<List<Color>>): String {
        return formatXml(createXmlFromColors(colorsGroup))
    }

    private fun createXmlFromColors(colorsGroup: List<List<Color>>): String {
        val directives = Directives().add(ROOT_ELEMENT_NAME)
        val xembler = Xembler(directives)
        colorsGroup.forEach { group ->
            if (group.isNotEmpty()) {
                group.forEach { color ->
                    directives
                        .add(CHILD_ELEMENT_NAME)
                        .attr(ATTRIBUTE_NAME, color.name)
                        .set(color.hex)
                        .up()
                }
                directives.add(CHILD_ELEMENT_SPACE).up()
            }
        }
        return xembler.xml()
    }

    private fun formatXml(xml: String): String {
        return xml.replace(OLD_VALUE_STANDALONE, EMPTY_STRING)
            .replace(OLD_VALUE_RESOURCES_START, NEW_VALUE_RESOURCES_START)
            .replace(OLD_VALUE_COLOR, NEW_VALUE_COLOR)
            .replace(OLD_VALUE_SPACE, "\n\t")
            .replace(OLD_VALUE_RESOURCES_END, NEW_VALUE_RESOURCES_END)
    }
}