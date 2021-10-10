package utils

import core.enums.Theme
import core.feature.colorFetch.enums.ColorType
import core.feature.colorFetch.model.Color

object ColorsHandler {

    fun handle(colors: List<Color>, theme: Theme): List<List<Color>> {
        val themeColors: MutableList<Color> = colors
            .filter { it.theme == theme }
            .toMutableList()
            .apply {
                if (theme == Theme.LIGHT) {
                    val constantColors = colors.filter { it.theme == Theme.CONSTANT }
                    this.addAll(0, constantColors)
                }
            }

        return sortColorByGroups(themeColors)
    }

    private fun sortColorByGroups(colors: List<Color>): List<List<Color>> {
        val colorsGroup: MutableList<List<Color>> = mutableListOf()
        ColorType.values().forEach { groupType ->
            val group: MutableList<Color> = mutableListOf()
            colors.forEach { color ->
                if (color.group == groupType) {
                    group.add(color)
                }
            }
            colorsGroup.add(group.toList())
        }
        return colorsGroup.toList()
    }

}