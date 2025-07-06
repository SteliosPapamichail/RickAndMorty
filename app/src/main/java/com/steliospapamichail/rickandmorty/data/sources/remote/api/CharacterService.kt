package com.steliospapamichail.rickandmorty.data.sources.remote.api

import com.steliospapamichail.rickandmorty.data.models.dtos.characters.AllEpisodeCharacters
import com.steliospapamichail.rickandmorty.data.models.dtos.characters.EpisodeCharacter
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface CharacterService {
    @GET("character")
    suspend fun getAllCharacters(
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap(),
    ): Response<AllEpisodeCharacters>

    @GET("character/{id}")
    suspend fun getCharacterDetails(
        @Path("id") characterId: Int,
    ): Response<EpisodeCharacter>

    @GET("character/{ids}")
    suspend fun getMultipleCharactersDetails(
        @Path("ids") characterIds: Array<Int>,
    ): Response<List<EpisodeCharacter>>
}