package com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episode_page_keys")
data class EpisodePageKey(
    @PrimaryKey val page: Int = 0,
    val nextPage: Int?,
    val prevPage: Int?
)
