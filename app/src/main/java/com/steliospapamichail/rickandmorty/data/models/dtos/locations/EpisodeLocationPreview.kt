package com.steliospapamichail.rickandmorty.data.models.dtos.locations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeLocationPreview(
    @SerialName("url")
    val url: String,
    @SerialName("name")
    val name: String,
)
