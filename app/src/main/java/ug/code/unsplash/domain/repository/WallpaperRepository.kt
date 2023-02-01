package ug.code.unsplash.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import ug.code.unsplash.domain.models.topic.Topic
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.domain.models.search.SearchPhoto

interface WallpaperRepository {

    suspend fun getLatestPhotos(
        clientId: String,
        page: Int,
    ): List<Photo>

    suspend fun getPhotoFromApiByID(
        id: String,
        clientId: String
    ): Photo

    suspend fun getTopics(
        clientId: String,
        page: Int,
        per_page: Int,
        order_by: String,
    ): List<Topic>

    suspend fun getImageSearch(
        witchQuery: String,
        query: String,
        color: String,
        order_by: String,
    ): LiveData<PagingData<Photo>>

    suspend fun getAllPhotosFromFavorite(): List<Photo>

    suspend fun getPhotoFromFavoriteByID(id: Int): Photo?

    suspend fun insertPhoto(data: Photo)

    suspend fun deletePhoto(data: Photo)
}