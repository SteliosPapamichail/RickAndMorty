package com.steliospapamichail.rickandmorty.data.mappers

import android.net.Uri
import com.steliospapamichail.rickandmorty.data.models.dtos.episodes.Episode
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodeDetailsEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodeDetails
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodePreview
import com.steliospapamichail.rickandmorty.utils.Consts.NOT_AVAILABLE
import com.steliospapamichail.rickandmorty.utils.formatEpisodeAirDate

fun EpisodePreviewEntity.toDomainModel(): EpisodePreview {
    return EpisodePreview(id, title, airedOn, code)
}

fun Episode.toDomainModel(): EpisodeDetails {
    return EpisodeDetails(
        airedOn = this.airDate.formatEpisodeAirDate() ?: NOT_AVAILABLE,
        characterIds = this.characters.mapNotNull {
            Uri.parse(it).lastPathSegment?.toIntOrNull()
        },
        episodeCode = this.episode,
        title = this.name
    )
}

fun EpisodeDetailsEntity.toDomainModel(): EpisodeDetails {
    return EpisodeDetails(
        airedOn = this.airedOn,
        characterIds = this.characterIds,
        episodeCode = this.code,
        title = this.title
    )
}