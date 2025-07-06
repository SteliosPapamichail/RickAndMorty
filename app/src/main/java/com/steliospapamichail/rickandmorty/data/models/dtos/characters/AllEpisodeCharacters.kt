package com.steliospapamichail.rickandmorty.data.models.dtos.characters


import com.steliospapamichail.rickandmorty.data.models.dtos.common.PaginationInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllEpisodeCharacters(
    @SerialName("info")
    val paginationInfo: PaginationInfo,
    @SerialName("results")
    val characters: List<EpisodeCharacter>
)