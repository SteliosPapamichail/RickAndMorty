package com.steliospapamichail.rickandmorty.data.sources.remote.api

import com.steliospapamichail.rickandmorty.data.models.dtos.episodes.AllEpisodes
import com.steliospapamichail.rickandmorty.data.models.dtos.episodes.Episode
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface EpisodeService {
    @GET("episode")
    suspend fun getAllEpisodes(
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap(),
    ): Response<AllEpisodes>

    @GET("episode/{id}")
    suspend fun getEpisodeDetails(
        @Path("id") episodeId: Int,
    ): Response<Episode>

    @GET("episode/{ids}")
    suspend fun getMultipleEpisodesDetails(
        @Path("ids") episodeIds: Array<Int>,
    ): Response<List<Episode>>
}