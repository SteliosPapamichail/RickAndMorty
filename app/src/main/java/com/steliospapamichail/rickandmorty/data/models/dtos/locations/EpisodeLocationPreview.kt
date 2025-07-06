package com.steliospapamichail.rickandmorty.data.models.dtos.locations

import android.net.Uri
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeLocationPreview(
    @SerialName("url")
    val url: String,
    @SerialName("name")
    val name: String,
) {
    fun getIdFromUrl(): Int? {
        return Uri.parse(url).lastPathSegment?.toIntOrNull()
    }
}
