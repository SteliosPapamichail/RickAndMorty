package com.steliospapamichail.rickandmorty.data.repositories.episodes

import androidx.paging.PagingSource
import com.steliospapamichail.rickandmorty.data.models.common.Resource
import com.steliospapamichail.rickandmorty.data.models.dtos.episodes.Episode
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodeDetails
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {
    fun getAllEpisodes(filters: Map<String,String> = emptyMap()) : PagingSource<Int, EpisodePreviewEntity>
    suspend fun getEpisodeDetailsAsFlow(episodeId:Int) : Flow<Resource<EpisodeDetails>>
}