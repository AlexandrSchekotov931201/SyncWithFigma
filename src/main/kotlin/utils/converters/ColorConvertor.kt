package utils.converters

import okhttp3.internal.format
import kotlin.math.roundToInt

object ColorConvertor {
    private const val HEX_FORMAT = "#%02x%02x%02x"

    fun rgbToHex(r: Float, g: Float, b: Float): String{
        return format(HEX_FORMAT, (r * 255).roundToInt(), (g * 255).roundToInt(), (b * 255).roundToInt())
    }
}