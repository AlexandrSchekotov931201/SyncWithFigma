package core.entities

import com.google.gson.annotations.SerializedName

data class GetFileStylesEntity(
    @SerializedName("error") val isError: Boolean,
    @SerializedName("status") val status: Int,
    @SerializedName("meta") val meta: MetaStylesEntity
)

data class MetaStylesEntity(
    @SerializedName("styles") val styles: List<StylesEntity>
)

data class StylesEntity(
    @SerializedName("node_id") val nodeId: String,
    @SerializedName("style_type") var styleType: String,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
)