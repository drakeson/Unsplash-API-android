package ug.code.unsplash.domain.models.search

import ug.code.unsplash.domain.models.photo.Photo

data class SearchPhoto(
    val total: Int,
    val total_pages: Int,
    val resultsPhoto: List<Photo>
)