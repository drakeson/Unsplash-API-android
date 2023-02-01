package ug.code.unsplash.domain.usecase.home

import ug.code.unsplash.domain.models.topic.Topic
import ug.code.unsplash.domain.repository.WallpaperRepository
import javax.inject.Inject

class GetTopicsUseCase @Inject constructor(
    private val repository: WallpaperRepository
) {

    suspend fun invoke(
        clientId: String,
        page: Int,
        per_page: Int,
        order_by: String,
    ): List<Topic> {
        return repository.getTopics(clientId, page, per_page, order_by)
    }
}