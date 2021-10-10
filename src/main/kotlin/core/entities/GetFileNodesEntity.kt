package core.entities

import com.google.gson.annotations.SerializedName

data class GetFileNodesEntity(
    @SerializedName("nodes") val nodes: Map<String, NodeDocumentEntity>
)

data class NodeDocumentEntity(
    @SerializedName("document") val document: DocumentEntity
)

data class DocumentEntity(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("fills") val fills: List<FillEntity>,
)

data class FillEntity(
    @SerializedName("opacity") val opacity: Float?,
    @SerializedName("color") var color: ColorEntity,
)

data class ColorEntity(
    @SerializedName("r") val r: Float,
    @SerializedName("g") var g: Float,
    @SerializedName("b") var b: Float,
    @SerializedName("a") var a: Float,
)