package entities

import com.google.gson.annotations.SerializedName

data class ImagesEntity(
    @SerializedName("err")val error: String,
    @SerializedName("images")val images: Map<String, String>?,
)