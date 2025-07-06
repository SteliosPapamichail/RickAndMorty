package com.steliospapamichail.rickandmorty.data.sources.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodeDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodePageKeysDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePageKey
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity

@Database(entities = [EpisodePreviewEntity::class, EpisodePageKey::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
    abstract fun episodePageKeysDao() : EpisodePageKeysDao
}