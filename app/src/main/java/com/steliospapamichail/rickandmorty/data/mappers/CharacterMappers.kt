package com.steliospapamichail.rickandmorty.data.mappers

import com.steliospapamichail.rickandmorty.data.models.dtos.characters.EpisodeCharacter
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails

fun EpisodeCharacter.toDomainModel(): CharacterDetails {
    return CharacterDetails(
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        origin = this.origin.toDomainModel(),
        location = this.location.toDomainModel(),
        imageUrl = this.image,
        episodesCount = this.episode.size,
    )
}