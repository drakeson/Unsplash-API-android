package ug.code.unsplash.data.remote.models.photo

import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("city")
    val city: String? = null,

    @SerializedName("country")
    val country: String? = null
)