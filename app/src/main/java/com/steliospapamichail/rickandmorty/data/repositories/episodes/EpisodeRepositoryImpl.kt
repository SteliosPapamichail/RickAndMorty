package com.steliospapamichail.rickandmorty.data.repositories.episodes

import android.util.Log
import androidx.paging.PagingSource
import com.steliospapamichail.rickandmorty.data.mappers.toDomainModel
import com.steliospapamichail.rickandmorty.data.models.shared.Resource
import com.steliospapamichail.rickandmorty.data.sources.local.db.AppDatabase
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodeDetailsEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.data.sources.remote.api.EpisodeService
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodeDetails
import com.steliospapamichail.rickandmorty.domain.repositories.episode.EpisodeRepository
import com.steliospapamichail.rickandmorty.domain.exceptions.NetworkRequestException
import com.steliospapamichail.rickandmorty.utils.DbRecordTTL.RECORD_TTL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock

class EpisodeRepositoryImpl(
    private val db: AppDatabase,
    private val episodeService: EpisodeService,
    private val ioDispatcher: CoroutineDispatcher,
) : EpisodeRepository {
    override fun getAllEpisodes(filters: Map<String, String>): PagingSource<Int, EpisodePreviewEntity> {
        return db.episodeDao().pagingSource()
    }

    override suspend fun getEpisodeDetailsAsFlow(episodeId: Int): Flow<Resource<EpisodeDetails>> = flow {
        emit(Resource.Loading)
        debugLog("Loading episode info for episode id $episodeId")
        val episodeDetails = db.episodeDetailsDao().getEpisodeDetailsFlow(episodeId).firstOrNull()

        val now = Clock.System.now().toEpochMilliseconds()
        if (episodeDetails != null && now < episodeDetails.expiresAt) {
            debugLog("Cache hit, returning fresh data")
            emit(Resource.Success(episodeDetails.toDomainModel()))
            return@flow
        }

        debugLog("Cache miss, fetching from network")
        fetchAndPersistEpisodeDetails(episodeId, episodeDetails)
    }.flowOn(ioDispatcher)

    private suspend fun FlowCollector<Resource<EpisodeDetails>>.fetchAndPersistEpisodeDetails(
        episodeId: Int,
        localEpisodeDetails: EpisodeDetailsEntity?,
    ) {
        try {
            val response = episodeService.getEpisodeDetails(episodeId)

            if (!response.isSuccessful || response.body() == null) {
                debugLog("Network request failed or response was invalid")
                emit(Resource.Error(NetworkRequestException("Failed to update episode info")))
                return
            }

            val expiresAt = Clock.System.now().toEpochMilliseconds() + RECORD_TTL
            val remoteEpisodeDetails = EpisodeDetailsEntity.fromDto(response.body()!!, expiresAt)
            debugLog("persisting episode details in DB")
            db.episodeDetailsDao().upsertEpisodeDetails(remoteEpisodeDetails)
            val updatedDetails = db.episodeDetailsDao()
                .getEpisodeDetailsFlow(episodeId)
                .first()!!
                .toDomainModel()
            emit(Resource.Success(updatedDetails))
        } catch (t: Throwable) {
            debugLog("Unknown error occurred")
            if (localEpisodeDetails != null) {
                debugLog("Returning stale data")
                val staleEpisodeDetails = localEpisodeDetails.toDomainModel()
                emit(Resource.Success(staleEpisodeDetails))
            } else {
                debugLog("Emitting error")
                emit(Resource.Error(t))
            }
        }
    }

    private fun debugLog(msg: String) {
        Log.d(EpisodeRepository::class.simpleName, msg)
    }
}