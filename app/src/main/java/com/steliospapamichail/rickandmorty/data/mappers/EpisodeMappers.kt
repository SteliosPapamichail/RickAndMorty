package com.steliospapamichail.rickandmorty.data.mappers

import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodeDetailsEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodeDetails
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodePreview

fun EpisodePreviewEntity.toDomainModel(): EpisodePreview {
    return EpisodePreview(id, title, airedOn, code)
}

fun EpisodeDetailsEntity.toDomainModel(): EpisodeDetails {
    return EpisodeDetails(
        airedOn = this.airedOn,
        characterIds = this.characterIds,
        episodeCode = this.code,
        title = this.title
    )
}