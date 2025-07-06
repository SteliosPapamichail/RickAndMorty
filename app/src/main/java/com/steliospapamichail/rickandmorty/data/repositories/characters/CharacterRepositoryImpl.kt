package com.steliospapamichail.rickandmorty.data.repositories.characters

import com.steliospapamichail.rickandmorty.data.mappers.toDomainModel
import com.steliospapamichail.rickandmorty.data.sources.remote.api.CharacterService
import com.steliospapamichail.rickandmorty.exceptions.InvalidPayloadException
import com.steliospapamichail.rickandmorty.exceptions.NetworkRequestException
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails
import retrofit2.HttpException
import java.io.IOException

class CharacterRepositoryImpl(private val characterService: CharacterService) : CharacterRepository {
    override suspend fun getCharacterDetails(charId: Int): Result<CharacterDetails> {
        return try {
            val response = characterService.getCharacterDetails(charId)
            val hasPayload = response.body() != null
            if (response.isSuccessful && hasPayload) {
                Result.success(response.body()!!.toDomainModel())
            } else if (!hasPayload) {
                Result.failure(InvalidPayloadException())
            } else {
                Result.failure(NetworkRequestException(response.errorBody().toString()))
            }
        } catch (ioEx: IOException) {
            Result.failure(ioEx)
        } catch (httpEx: HttpException) {
            Result.failure(httpEx)
        }
    }
}