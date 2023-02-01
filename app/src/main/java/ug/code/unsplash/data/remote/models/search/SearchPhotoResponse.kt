package ug.code.unsplash.data.remote.models.search

import com.google.gson.annotations.SerializedName
import ug.code.unsplash.data.remote.models.photo.PhotoResponse

data class SearchPhotoResponse(
    @SerializedName("total")
    val total: Int,
    @SerializedName("total_pages")
    val total_pages: Int,
    @SerializedName("results")
    val resultsPhoto: List<PhotoResponse>
)