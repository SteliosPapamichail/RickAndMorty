package com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.steliospapamichail.rickandmorty.data.models.dtos.episodes.Episode

@Entity("episode_details")
data class EpisodeDetailsEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("air_date") val airedOn: String,
    @ColumnInfo("code") val code: String,
    @ColumnInfo("character_ids")
    val characterIds: List<Int>,
    val expiresAt: Long,
) {
    companion object {
        fun fromDto(dto: Episode, ttlMillis: Long): EpisodeDetailsEntity {
            return EpisodeDetailsEntity(
                id = dto.id,
                title = dto.name,
                airedOn = dto.airDate,
                code = dto.episode,
                characterIds = dto.characters.mapNotNull {
                    Uri.parse(it).lastPathSegment?.toIntOrNull()
                },
                expiresAt = ttlMillis
            )
        }
    }
}
