package com.steliospapamichail.rickandmorty.models.episodes


import com.steliospapamichail.rickandmorty.models.common.PaginationInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllEpisodes(
    @SerialName("info")
    val paginationInfo: PaginationInfo,
    @SerialName("results")
    val episodes: List<Episode>
)