package ug.code.unsplash.data.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ug.code.unsplash.data.mapper.toPhoto
import ug.code.unsplash.data.mapper.toSearchPhoto
import ug.code.unsplash.data.remote.WallpaperService
import ug.code.unsplash.domain.models.photo.Photo
import ug.code.unsplash.util.Constants.Companion.CLIENT_ID
import ug.code.unsplash.util.Constants.Companion.PER_PAGE
import ug.code.unsplash.util.Constants.Companion.colors
import ug.code.unsplash.util.Constants.Companion.latest
import ug.code.unsplash.util.Constants.Companion.search
import ug.code.unsplash.util.Constants.Companion.topics
import retrofit2.HttpException
import java.io.IOException

class PhotoPagingSource(
    private val wallpaperService: WallpaperService,
    private val witchQuery: String,
    private val query: String,
    private val color: String,
    private val order_by: String
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val  anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val pageNumber = params.key ?: 1
            val response = when(witchQuery) {
                search -> {
                    wallpaperService.getSearchImage(
                        query, CLIENT_ID, pageNumber, PER_PAGE, order_by
                    ).toSearchPhoto().resultsPhoto
                }
                colors -> {
                    wallpaperService.getColorImage(query, color, CLIENT_ID, pageNumber, PER_PAGE, order_by).toSearchPhoto().resultsPhoto
                }
                topics -> {
                    wallpaperService.getTopicImage(query, CLIENT_ID, pageNumber, PER_PAGE, order_by).map { it.toPhoto() }
                }
                latest -> {
                    wallpaperService.getLatest(CLIENT_ID, pageNumber, PER_PAGE, order_by).map { it.toPhoto() }
                }
                else -> {}
            }
            LoadResult.Page(
                data = response as List<Photo>,
                prevKey = null,
                nextKey = pageNumber.plus(1)
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}