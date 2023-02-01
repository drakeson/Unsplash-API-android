package ug.code.unsplash.domain.usecase.detailI

import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.domain.repository.WallpaperRepository
import javax.inject.Inject

class GetPhotoFromFavoriteByIDUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {

    suspend fun invoke(
        id_photo: Int
    ): Photo? {
        return repository.getPhotoFromFavoriteByID(id_photo)
    }
}