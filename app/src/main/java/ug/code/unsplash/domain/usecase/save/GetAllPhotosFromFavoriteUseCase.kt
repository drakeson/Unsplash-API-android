package ug.code.unsplash.domain.usecase.save

import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.domain.repository.WallpaperRepository
import javax.inject.Inject

class GetAllPhotosFromFavoriteUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {

    suspend fun invoke(): List<Photo> {
        return repository.getAllPhotosFromFavorite()
    }
}