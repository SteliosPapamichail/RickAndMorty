package com.steliospapamichail.rickandmorty.data.sources.remote.api

import com.steliospapamichail.rickandmorty.data.models.dtos.locations.AllEpisodeLocations
import com.steliospapamichail.rickandmorty.data.models.dtos.locations.EpisodeLocation
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface LocationService {
    @GET("location")
    suspend fun getAllLocations(
        @Query("page") page: Int = 1,
        @QueryMap filters: Map<String, String> = emptyMap(),
    ): Response<AllEpisodeLocations>

    @GET("location/{id}")
    suspend fun getLocationDetails(
        @Path("id") locationId: Int,
    ): Response<EpisodeLocation>

    @GET("location/{ids}")
    suspend fun getMultipleLocationsDetails(
        @Path("ids") locationIds: Array<Int>,
    ): Response<List<EpisodeLocation>>
}