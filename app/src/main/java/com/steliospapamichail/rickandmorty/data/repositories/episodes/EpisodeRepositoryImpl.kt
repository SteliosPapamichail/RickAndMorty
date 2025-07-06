package com.steliospapamichail.rickandmorty.data.repositories.episodes

import androidx.paging.PagingSource
import com.steliospapamichail.rickandmorty.data.mappers.toDomainModel
import com.steliospapamichail.rickandmorty.data.models.dtos.episodes.Episode
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodeDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.data.sources.remote.api.EpisodeService
import com.steliospapamichail.rickandmorty.exceptions.InvalidPayloadException
import com.steliospapamichail.rickandmorty.exceptions.NetworkRequestException
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodeDetails
import retrofit2.HttpException
import java.io.IOException

class EpisodeRepositoryImpl(
    private val episodeDao: EpisodeDao,
    private val episodeService: EpisodeService,
) : EpisodeRepository {
    override fun getAllEpisodes(filters: Map<String, String>): PagingSource<Int, EpisodePreviewEntity> {
        return episodeDao.pagingSource()
    }

    override suspend fun getEpisodeDetails(episodeId: Int): Result<EpisodeDetails> {
        return try {
            val response = episodeService.getEpisodeDetails(episodeId)
            val hasPayload = response.body() != null
            if (response.isSuccessful && hasPayload) {
                Result.success(response.body()!!.toDomainModel())
            } else if (!hasPayload) {
                Result.failure(InvalidPayloadException())
            } else {
                Result.failure(NetworkRequestException(response.errorBody().toString()))
            }
        } catch (ioEx: IOException) {
            Result.failure(ioEx)
        } catch (httpEx: HttpException) {
            Result.failure(httpEx)
        }
    }

    override suspend fun getMultipleEpisodes(episodeIds: Array<Int>): Result<List<Episode>> {
        TODO("Not yet implemented")
    }
}