package core.interfaces

import entities.FileComponentsEntity
import entities.ImagesEntity

interface FigmaApi {
    fun getImages(nodeIds: String): ImagesEntity?
    fun getFileComponents(): FileComponentsEntity?
}