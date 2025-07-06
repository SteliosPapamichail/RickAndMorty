package com.steliospapamichail.rickandmorty.data.models.dtos.episodes


import com.steliospapamichail.rickandmorty.data.models.dtos.common.PaginationInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllEpisodes(
    @SerialName("info")
    val paginationInfo: PaginationInfo,
    @SerialName("results")
    val episodes: List<Episode>
)