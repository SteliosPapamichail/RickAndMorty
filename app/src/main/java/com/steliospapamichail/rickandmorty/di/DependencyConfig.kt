package com.steliospapamichail.rickandmorty.di

import com.steliospapamichail.rickandmorty.networking.api.CharacterService
import com.steliospapamichail.rickandmorty.networking.api.EpisodeService
import com.steliospapamichail.rickandmorty.networking.api.LocationService
import com.steliospapamichail.rickandmorty.networking.client.RetrofitClient
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single<Retrofit> { RetrofitClient().retrofitClient }
    single<CharacterService> { get<Retrofit>().create(CharacterService::class.java) }
    single<EpisodeService> { get<Retrofit>().create(EpisodeService::class.java) }
    single<LocationService> { get<Retrofit>().create(LocationService::class.java) }
}