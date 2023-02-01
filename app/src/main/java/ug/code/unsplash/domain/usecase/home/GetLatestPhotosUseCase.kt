package ug.code.unsplash.domain.usecase.home

import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.domain.repository.WallpaperRepository
import javax.inject.Inject

class GetLatestPhotosUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {

    suspend fun invoke(
        clientId: String,
        page: Int
    ): List<Photo> {
        return repository.getLatestPhotos(clientId, page)
    }
}