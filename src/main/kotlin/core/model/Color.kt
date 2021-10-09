package core.model

import utils.converters.ColorConvertor

data class Color(
    val name: String,
    private val red: Float,
    private val green: Float,
    private val blue: Float,
    private val alpha: Float
) {

    var hex: String
    private set

    init {
        this.hex = ColorConvertor.rgbToHex(red, green, blue)
    }
}