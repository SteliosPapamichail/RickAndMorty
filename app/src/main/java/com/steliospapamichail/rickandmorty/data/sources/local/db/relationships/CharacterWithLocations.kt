package com.steliospapamichail.rickandmorty.data.sources.local.db.relationships

import androidx.room.Embedded
import androidx.room.Relation
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.characters.CharacterEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.locations.LocationEntity

data class CharacterWithLocations(
    @Embedded val character: CharacterEntity,
    @Relation(
        parentColumn = "origin_location_id",
        entityColumn = "id"
    ) val originLocation: LocationEntity?,
    @Relation(
        parentColumn = "location_id",
        entityColumn = "id"
    ) val location: LocationEntity?,
)