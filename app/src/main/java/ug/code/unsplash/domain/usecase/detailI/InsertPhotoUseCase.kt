package ug.code.unsplash.domain.usecase.detailI

import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.domain.repository.WallpaperRepository
import javax.inject.Inject

class InsertPhotoUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {

    suspend fun invoke(
        photo: Photo
    ) {
        return repository.insertPhoto(photo)
    }
}