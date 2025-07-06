package com.steliospapamichail.rickandmorty.data.models.dtos.characters


import com.steliospapamichail.rickandmorty.data.models.dtos.locations.EpisodeLocationPreview
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeCharacter(
    @SerialName("created")
    val created: String,
    @SerialName("episode")
    val episode: List<String>,
    @SerialName("gender")
    val gender: String,
    @SerialName("id")
    val id: Int,
    @SerialName("image")
    val image: String,
    @SerialName("location")
    val location: EpisodeLocationPreview?,
    @SerialName("name")
    val name: String,
    @SerialName("origin")
    val origin: EpisodeLocationPreview?,
    @SerialName("species")
    val species: String,
    @SerialName("status")
    val status: String,
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String,
)