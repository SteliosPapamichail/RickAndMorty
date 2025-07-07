package com.steliospapamichail.rickandmorty.data.mappers

import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.characters.CharacterEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.locations.LocationEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.relationships.CharacterWithLocations
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails

fun CharacterWithLocations.toDomainModel(): CharacterDetails {
    return CharacterDetails(
        name = this.character.name,
        status = this.character.status,
        species = this.character.species,
        type = this.character.type,
        gender = this.character.gender,
        origin = this.originLocation?.toDomainModel(),
        location = this.location?.toDomainModel(),
        imageUrl = this.character.imageUrl,
        episodesCount = this.character.episodesCount
    )
}

fun CharacterEntity.toDomainModel(origin: LocationEntity?, location: LocationEntity?): CharacterDetails {
    return CharacterDetails(
        name = this.name,
        status = this.status,
        species = this.species,
        type = this.type,
        gender = this.gender,
        origin = origin?.toDomainModel(),
        location = location?.toDomainModel(),
        imageUrl = this.imageUrl,
        episodesCount = this.episodesCount
    )
}