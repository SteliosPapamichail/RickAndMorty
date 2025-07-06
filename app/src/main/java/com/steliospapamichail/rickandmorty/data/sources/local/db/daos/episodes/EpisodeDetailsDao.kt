package com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodeDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDetailsDao {
    @Transaction
    @Query("SELECT * FROM episode_details WHERE id = :episodeId")
    fun getEpisodeDetailsFlow(episodeId: Int): Flow<EpisodeDetailsEntity?>

    @Upsert
    suspend fun upsertEpisodeDetails(detail: EpisodeDetailsEntity)
}