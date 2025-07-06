package com.steliospapamichail.rickandmorty.data.sources.local.db.daos.locations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.locations.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(location: LocationEntity)

    @Query("SELECT * FROM locations WHERE id = :id")
    fun getByIdFlow(id: String): Flow<LocationEntity?>
}