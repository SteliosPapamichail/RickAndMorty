package com.steliospapamichail.rickandmorty.data.sources.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.characters.CharacterDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodeDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodePageKeysDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.locations.LocationDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.characters.CharacterEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePageKey
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.locations.LocationEntity

@Database(
    entities = [
        EpisodePreviewEntity::class, EpisodePageKey::class,
        CharacterEntity::class, LocationEntity::class
    ], version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun episodeDao(): EpisodeDao
    abstract fun episodePageKeysDao(): EpisodePageKeysDao
    abstract fun locationDao(): LocationDao
    abstract fun characterDao(): CharacterDao
}