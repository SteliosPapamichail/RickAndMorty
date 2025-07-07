package com.steliospapamichail.rickandmorty.data.mediators

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.steliospapamichail.rickandmorty.data.sources.local.db.AppDatabase
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodeDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodePageKeysDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePageKeyEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.data.sources.remote.api.EpisodeService
import com.steliospapamichail.rickandmorty.utils.AppDataStore
import kotlinx.datetime.Clock
import retrofit2.HttpException
import java.io.IOException


@OptIn(ExperimentalPagingApi::class)
class EpisodeRemoteMediator(
    private val database: AppDatabase,
    private val networkService: EpisodeService,
    private val appDataStore: AppDataStore,
) : RemoteMediator<Int, EpisodePreviewEntity>() {
    private val episodeDao: EpisodeDao = database.episodeDao()
    private val episodePageKeysDao: EpisodePageKeysDao = database.episodePageKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, EpisodePreviewEntity>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull() ?: return MediatorResult.Success(endOfPaginationReached = false)
                    val key = episodePageKeysDao.getKeyByPage(lastItem.page) ?: return MediatorResult.Success(endOfPaginationReached = true)
                    key.nextPage ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val episodesResponse = networkService.getAllEpisodes(page = page)
            if (!episodesResponse.isSuccessful) return MediatorResult.Error(IllegalStateException("Network request failed"))
            else if (episodesResponse.body() == null) return MediatorResult.Error(IllegalStateException("Empty body"))

            val info = episodesResponse.body()!!.paginationInfo
            val nextPage = info.next?.let { Uri.parse(it).getQueryParameter("page")?.toInt() }
            val prevPage = info.prev?.let { Uri.parse(it).getQueryParameter("page")?.toInt() }
            val episodes = episodesResponse.body()!!.episodes.map { EpisodePreviewEntity.fromDto(it, page) }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    episodeDao.clearAll()
                    episodePageKeysDao.clearAll()
                }
                episodeDao.insertAll(episodes)
                episodePageKeysDao.insert(
                    EpisodePageKeyEntity(page = page, nextPage = nextPage, prevPage = prevPage)
                )
                appDataStore.updateLastRefreshTimestamp(Clock.System.now().toEpochMilliseconds())
            }

            MediatorResult.Success(
                endOfPaginationReached = info.next == null
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}