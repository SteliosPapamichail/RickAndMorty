package com.steliospapamichail.rickandmorty.data.models.dtos.locations

import com.steliospapamichail.rickandmorty.data.models.dtos.common.PaginationInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllEpisodeLocations(
    @SerialName("info")
    val paginationInfo: PaginationInfo,
    @SerialName("results")
    val locations: List<EpisodeLocation>,
)