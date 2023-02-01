package ug.code.unsplash.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import ug.code.unsplash.data.local.WallpaperDao
import ug.code.unsplash.data.mapper.toPhoto
import ug.code.unsplash.data.mapper.toPhotoEntity
import ug.code.unsplash.data.mapper.toTopic
import ug.code.unsplash.data.pagingsources.PhotoPagingSource
import ug.code.unsplash.data.remote.WallpaperService
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.domain.models.topic.Topic
import ug.code.unsplash.domain.repository.WallpaperRepository
import ug.code.unsplash.util.TopicsOrder
import javax.inject.Inject

class WallpaperRepositoryImpl @Inject constructor(
    private var wallpaperService: WallpaperService,
    private val wallpaperDao: WallpaperDao
) : WallpaperRepository {

    override suspend fun getLatestPhotos(
        clientId: String,
        page: Int,
    ): List<Photo> {
        return wallpaperService.getLatest(clientId, page, 10, TopicsOrder.LATEST.query).map { it.toPhoto() }
    }

    override suspend fun getPhotoFromApiByID(
        id: String,
        clientId: String
    ): Photo {
        return wallpaperService.getPhoto(id, clientId).toPhoto()
    }

    override suspend fun getTopics(
        clientId: String,
        page: Int,
        per_page: Int,
        order_by: String,
    ): List<Topic> {
        return wallpaperService.getTopicsList(clientId, page, per_page, order_by)
            .map { it.toTopic() }
    }

    override suspend fun getImageSearch(
        witchQuery: String,
        query: String,
        color: String,
        order_by: String,
    ): LiveData<PagingData<Photo>> {
        return Pager(config = PagingConfig(pageSize = 1, enablePlaceholders = false)) {
            PhotoPagingSource(wallpaperService, witchQuery, query, color, order_by)
        }.liveData
    }

    override suspend fun getAllPhotosFromFavorite(): List<Photo> {
        return wallpaperDao.getAllPhoto().map { it.toPhoto() }
    }

    override suspend fun getPhotoFromFavoriteByID(id: Int): Photo? {
        return wallpaperDao.getById(id)?.toPhoto()
    }

    override suspend fun insertPhoto(data: Photo) {
        return wallpaperDao.insertPhoto(data.toPhotoEntity())
    }

    override suspend fun deletePhoto(data: Photo) {
        return wallpaperDao.deletePhoto(data.toPhotoEntity())
    }
}