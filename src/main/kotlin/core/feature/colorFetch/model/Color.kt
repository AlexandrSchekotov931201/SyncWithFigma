package core.feature.colorFetch.model

import Config.DARK
import Config.LIGHT
import Config.MTS_CAML_CASE
import Config.MTS_CAPS
import Config.PREFIX_DS
import core.enums.Theme
import core.feature.colorFetch.enums.ColorType
import utils.converters.ColorToColorConvertor
import utils.ex.*

data class Color(
    var name: String,
    private val red: Float,
    private val green: Float,
    private val blue: Float,
    private val alpha: Float
) {

    var theme: Theme
        private set

    var group: ColorType?
        private set

    var hex: String
        private set

    init {
        theme = when {
            name.contains(LIGHT) -> Theme.LIGHT
            name.contains(DARK) -> Theme.DARK
            else -> Theme.CONSTANT
        }
        group = getGroup(name)
        name = formatName(name)
        hex = ColorToColorConvertor.convertRGBToHEX(red, green, blue).uppercase()
    }

    private fun getGroup(colorName: String): ColorType? {
        var receivedColorType: ColorType? = null
        val groupTypeFromColorName = colorName.split("/")[0].lowercase()
        ColorType.values().forEach { groupType ->
            if (groupTypeFromColorName.contains(groupType.value)) {
                receivedColorType = groupType
            }
        }
        return receivedColorType
    }

    private fun formatName(name: String): String {
        return name
            .replace(MTS_CAPS, MTS_CAML_CASE)
            .replace(LIGHT, EMPTY_STRING)
            .replace(DARK, EMPTY_STRING)
            .replace(specialCharactersRegex, EMPTY_STRING)
            .toSnakeCase()
            .appendPrefix(PREFIX_DS)
            .replace("$PREFIX_DS${ColorType.CONSTANT.value}$UNDERSCORE", PREFIX_DS)
    }
}