package com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.steliospapamichail.rickandmorty.data.models.dtos.episodes.Episode
import com.steliospapamichail.rickandmorty.utils.formatEpisodeAirDate

@Entity(tableName = "episode_previews")
data class EpisodePreviewEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("air_date") val airedOn: String,
    @ColumnInfo("code") val code: String,
    @ColumnInfo("page") val page: Int,
) {
    companion object {
        fun fromDto(dto: Episode, page: Int): EpisodePreviewEntity {
            return EpisodePreviewEntity(dto.id, dto.name, dto.airDate.formatEpisodeAirDate() ?: "N/A", dto.episode, page)
        }
    }
}