package com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity

@Dao
interface EpisodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(episodePreviews: List<EpisodePreviewEntity>)

    @Query("DELETE FROM episode_previews WHERE page=:page")
    suspend fun deleteByPage(page: Int)

    @Query("SELECT * FROM episode_previews")
    fun pagingSource(): PagingSource<Int, EpisodePreviewEntity>

    @Query("DELETE FROM episode_previews")
    suspend fun clearAll()
}