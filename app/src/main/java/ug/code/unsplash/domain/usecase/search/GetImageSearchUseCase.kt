package ug.code.unsplash.domain.usecase.search

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.domain.repository.WallpaperRepository
import javax.inject.Inject

class GetImageSearchUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {

    suspend fun invoke(
        witchQuery: String,
        query: String,
        color: String,
        order_by: String,
    ): LiveData<PagingData<Photo>> {
        return repository.getImageSearch(witchQuery, query, color, order_by)
    }
}