package com.steliospapamichail.rickandmorty.data.mappers

import android.net.Uri
import com.steliospapamichail.rickandmorty.data.models.dtos.locations.EpisodeLocationPreview
import com.steliospapamichail.rickandmorty.domain.models.locations.LocationPreview

fun EpisodeLocationPreview.toDomainModel() : LocationPreview {
    val locationId = Uri.parse(this.url).lastPathSegment?.toIntOrNull()
    return LocationPreview(
        id = locationId ?: -1,
        name = this.name
    )
}