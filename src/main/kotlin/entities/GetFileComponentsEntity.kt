package entities

import com.google.gson.annotations.SerializedName

data class FileComponentsEntity(
    @SerializedName("error") val isError: Boolean,
    @SerializedName("status") val status: Int,
    @SerializedName("meta") val meta: MetaEntity
)

data class MetaEntity(
    @SerializedName("components") val components: List<Component>
)

data class Component(
    @SerializedName("node_id") val nodeId: String,
    @SerializedName("name") var name: String,
    @SerializedName("description") val description: String,
    @SerializedName("containing_frame") val containingFrame: ContainingFrame
)

data class ContainingFrame(
    @SerializedName("pageName") val pageName: String
)