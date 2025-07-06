package com.steliospapamichail.rickandmorty.data.repositories.characters

import android.util.Log
import com.steliospapamichail.rickandmorty.data.mappers.toDomainModel
import com.steliospapamichail.rickandmorty.data.models.common.Resource
import com.steliospapamichail.rickandmorty.data.sources.local.db.AppDatabase
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.characters.CharacterEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.locations.LocationEntity
import com.steliospapamichail.rickandmorty.data.sources.local.db.relationships.CharacterWithLocations
import com.steliospapamichail.rickandmorty.data.sources.remote.api.CharacterService
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails
import com.steliospapamichail.rickandmorty.exceptions.NetworkRequestException
import com.steliospapamichail.rickandmorty.utils.DbRecordTTL.RECORD_TTL
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.datetime.Clock

class CharacterRepositoryImpl(
    private val characterService: CharacterService,
    private val db: AppDatabase,
    private val ioDispatcher: CoroutineDispatcher,
) : CharacterRepository {

    override suspend fun getCharacterDetailsAsFlow(charId: Int): Flow<Resource<CharacterDetails>> = flow {
        emit(Resource.Loading)
        debugLog("Loading character info for char id $charId")
        val characterWithLocations = db.characterDao().getCharacterWithLocationsFlow(charId).firstOrNull()
        val now = Clock.System.now().toEpochMilliseconds()
        if (characterWithLocations != null && now < characterWithLocations.character.expiresAt) {
            debugLog("Cache hit, returning fresh data")
            emit(Resource.Success(characterWithLocations.toDomainModel()))
            return@flow
        }

        debugLog("Cache miss, fetching from network")
        fetchAndPersistCharacterDetails(charId, characterWithLocations)
    }.flowOn(ioDispatcher)

    private suspend fun FlowCollector<Resource<CharacterDetails>>.fetchAndPersistCharacterDetails(
        charId: Int,
        characterWithLocations: CharacterWithLocations?,
    ) {
        try {
            val response = characterService.getCharacterDetails(charId)
            var origin: LocationEntity? = null
            var location: LocationEntity? = null

            if (!response.isSuccessful || response.body() == null) {
                debugLog("Network request failed or response was invalid")
                emit(Resource.Error(NetworkRequestException("Failed to update character info")))
                return
            }

            LocationEntity.fromDto(response.body()?.origin)?.let {
                debugLog("persisting character origin in DB")
                db.locationDao().upsert(it)
                origin = it
            }
            LocationEntity.fromDto(response.body()?.location)?.let {
                debugLog("persisting character location in DB")
                db.locationDao().upsert(it)
                location = it
            }

            val expiresAt = Clock.System.now().toEpochMilliseconds() + RECORD_TTL
            val charEnt = CharacterEntity.fromDto(response.body()!!, expiresAt)

            debugLog("persisting character details in DB")
            db.characterDao().upsert(charEnt)

            emit(Resource.Success(charEnt.toDomainModel(origin, location)))
        } catch (t: Throwable) {
            debugLog("Unknown error occurred")
            if (characterWithLocations != null) {
                debugLog("Returning stale data")
                val staleCharacterDetails = characterWithLocations.toDomainModel()
                emit(Resource.Success(staleCharacterDetails))
            } else {
                debugLog("Emitting error")
                emit(Resource.Error(t))
            }
        }
    }

    private fun debugLog(msg: String) {
        Log.d(CharacterRepository::class.simpleName, msg)
    }
}