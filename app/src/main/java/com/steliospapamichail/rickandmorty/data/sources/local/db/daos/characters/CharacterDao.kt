package com.steliospapamichail.rickandmorty.data.sources.local.db.daos.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.characters.CharacterEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.relationships.CharacterWithLocations
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Transaction
    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacterWithLocationsFlow(id: Int): Flow<CharacterWithLocations?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(character: CharacterEntity)
}