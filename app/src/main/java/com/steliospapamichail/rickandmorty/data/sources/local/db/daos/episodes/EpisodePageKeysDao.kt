package com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePageKeyEntity

@Dao
interface EpisodePageKeysDao {
    @Query("SELECT page FROM episode_page_keys")
    suspend fun getAllSavedPages(): List<Int>

    @Query("SELECT * FROM episode_page_keys WHERE page=:page")
    suspend fun getKeyByPage(page:Int) : EpisodePageKeyEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(keys: EpisodePageKeyEntity)

    @Query("DELETE FROM episode_page_keys")
    suspend fun clearAll()
}