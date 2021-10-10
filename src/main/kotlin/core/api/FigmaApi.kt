package core.api

import core.entities.FileComponentsEntity
import core.entities.GetFileNodesEntity
import core.entities.GetFileStylesEntity
import core.entities.ImagesEntity

interface FigmaApi {
    fun getFileNodes(nodeIds: String): GetFileNodesEntity?
    fun getImages(nodeIds: String): ImagesEntity?
    fun getFileComponents(): FileComponentsEntity?
    fun getFileStyles(): GetFileStylesEntity?
}