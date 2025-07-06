package com.steliospapamichail.rickandmorty.ui.screens.episodes.overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.steliospapamichail.rickandmorty.data.mappers.toDomainModel
import com.steliospapamichail.rickandmorty.data.mediators.EpisodeRemoteMediator
import com.steliospapamichail.rickandmorty.data.repositories.episodes.EpisodeRepository
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodePreview
import com.steliospapamichail.rickandmorty.utils.AppDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class EpisodesOverviewViewModel(
    private val episodesRepository: EpisodeRepository,
    private val appDataStore: AppDataStore,
    episodeRemoteMediator: EpisodeRemoteMediator,
) : ViewModel() {

    val lastRefreshTimestamp = appDataStore.lastRefreshFlow

    @OptIn(ExperimentalPagingApi::class)
    val episodePreviewsPaged: Flow<PagingData<EpisodePreview>> = Pager(
        config = PagingConfig(
            initialLoadSize = 20,
            pageSize = 20,
            //note: You can reduce the prefetchDistance to i.e. 2 or 3
            // to have a chance to see the loader at the bottom of the UI,
            // otherwise the prefetching is too fast usually (or mock a low network)
            prefetchDistance = 40,
            enablePlaceholders = false
        ),
        remoteMediator = episodeRemoteMediator
    ) {
        episodesRepository.getAllEpisodes()
    }.flow
        .map { value: PagingData<EpisodePreviewEntity> ->
            value.map { entity ->
                entity.toDomainModel()
            }
        }.cachedIn(viewModelScope)

    fun updateLastRefreshTimestamp() {
        println("called in vm")
        viewModelScope.launch {
            appDataStore.updateLastRefreshTimestamp()
        }
    }
}