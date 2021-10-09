package core.interfaces

import entities.FileComponentsEntity
import entities.GetFileNodesEntity
import entities.GetFileStylesEntity
import entities.ImagesEntity

interface FigmaApi {
    fun getFileNodes(nodeIds: String): GetFileNodesEntity?
    fun getImages(nodeIds: String): ImagesEntity?
    fun getFileComponents(): FileComponentsEntity?
    fun getFileStyles(): GetFileStylesEntity?
}