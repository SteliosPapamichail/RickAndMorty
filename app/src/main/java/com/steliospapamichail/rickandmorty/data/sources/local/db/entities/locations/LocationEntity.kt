package com.steliospapamichail.rickandmorty.data.sources.local.db.entities.locations

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.steliospapamichail.rickandmorty.data.models.dtos.locations.EpisodeLocationPreview

@Entity("locations")
data class LocationEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo("location_name")
    val name: String,
    @ColumnInfo("location_url")
    val url: String,
) {
    companion object {
        fun fromDto(location: EpisodeLocationPreview?): LocationEntity? {
            val id = location?.getIdFromUrl()
            return if (location == null || id == null) null
            else LocationEntity(id = id, name = location.name, url = location.url)
        }
    }
}
