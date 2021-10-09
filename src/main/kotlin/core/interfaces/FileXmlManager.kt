package core.interfaces

import core.model.Color

interface FileXmlManager {
    fun createXmlFileFromColor(fileName: String, colors: List<Color>)
}