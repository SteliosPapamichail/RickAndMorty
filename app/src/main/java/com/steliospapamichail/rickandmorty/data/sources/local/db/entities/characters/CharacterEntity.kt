package com.steliospapamichail.rickandmorty.data.sources.local.db.entities.characters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.steliospapamichail.rickandmorty.data.models.dtos.characters.EpisodeCharacter
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.locations.LocationEntity

@Entity(
    tableName = "characters",
    foreignKeys = [
        ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["origin_location_id"],
            onDelete = ForeignKey.SET_NULL
        ), ForeignKey(
            entity = LocationEntity::class,
            parentColumns = ["id"],
            childColumns = ["location_id"],
            onDelete = ForeignKey.SET_NULL
        )],
    indices = [Index("origin_location_id"), Index("location_id")]
)
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    @ColumnInfo("origin_location_id")
    val originId: Int?,
    @ColumnInfo("location_id")
    val locationId: Int?,
    val imageUrl: String,
    val episodesCount: Int,
    val expiresAt: Long,
) {
    companion object {
        fun fromDto(character: EpisodeCharacter, expiresAt: Long): CharacterEntity {
            return CharacterEntity(
                id = character.id,
                name = character.name,
                status = character.status,
                species = character.species,
                type = character.type,
                gender = character.gender,
                originId = LocationEntity.fromDto(character.origin)?.id,
                locationId = LocationEntity.fromDto(character.location)?.id,
                imageUrl = character.image,
                episodesCount = character.episode.size,
                expiresAt = expiresAt
            )
        }
    }
}
