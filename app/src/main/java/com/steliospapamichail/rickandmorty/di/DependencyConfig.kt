package com.steliospapamichail.rickandmorty.di

import androidx.room.Room
import com.steliospapamichail.rickandmorty.data.mediators.EpisodeRemoteMediator
import com.steliospapamichail.rickandmorty.data.networking.client.RetrofitClient
import com.steliospapamichail.rickandmorty.data.repositories.characters.CharacterRepository
import com.steliospapamichail.rickandmorty.data.repositories.characters.CharacterRepositoryImpl
import com.steliospapamichail.rickandmorty.data.repositories.episodes.EpisodeRepository
import com.steliospapamichail.rickandmorty.data.repositories.episodes.EpisodeRepositoryImpl
import com.steliospapamichail.rickandmorty.data.sources.local.db.AppDatabase
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.characters.CharacterDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodeDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.episodes.EpisodePageKeysDao
import com.steliospapamichail.rickandmorty.data.sources.local.db.daos.locations.LocationDao
import com.steliospapamichail.rickandmorty.data.sources.remote.api.CharacterService
import com.steliospapamichail.rickandmorty.data.sources.remote.api.EpisodeService
import com.steliospapamichail.rickandmorty.data.sources.remote.api.LocationService
import com.steliospapamichail.rickandmorty.domain.exporters.DocumentExporter
import com.steliospapamichail.rickandmorty.domain.usecases.ExportCharacterUseCase
import com.steliospapamichail.rickandmorty.ui.screens.characters.details.CharacterDetailsViewModel
import com.steliospapamichail.rickandmorty.ui.screens.episodes.details.EpisodeDetailsViewModel
import com.steliospapamichail.rickandmorty.ui.screens.episodes.overview.EpisodesOverviewViewModel
import com.steliospapamichail.rickandmorty.utils.AppDataStore
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    // Remote data source
    single<Retrofit> { RetrofitClient().retrofitClient }
    single<CharacterService> { get<Retrofit>().create(CharacterService::class.java) }
    single<EpisodeService> { get<Retrofit>().create(EpisodeService::class.java) }
    single<LocationService> { get<Retrofit>().create(LocationService::class.java) }

    // Room
    single<AppDatabase> {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java, "ricknmorty-db"
        )
            .fallbackToDestructiveMigration(false).build()
    }
    single<EpisodeDao> { get<AppDatabase>().episodeDao() }
    single<EpisodePageKeysDao> { get<AppDatabase>().episodePageKeysDao() }
    single<CharacterDao> { get<AppDatabase>().characterDao() }
    single<LocationDao> { get<AppDatabase>().locationDao() }

    // paging
    singleOf(::EpisodeRemoteMediator)

    // datastore prefs
    single { AppDataStore(androidContext()) }

    // Repositories
    single<EpisodeRepository> { EpisodeRepositoryImpl(get(), get()) }
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get(), get(named("ioDispatcher"))) }

    // use cases
    single { ExportCharacterUseCase(get()) }

    // ViewModels
    viewModelOf(::EpisodesOverviewViewModel)
    viewModelOf(::EpisodeDetailsViewModel)
    viewModelOf(::CharacterDetailsViewModel)

    single(named("ioDispatcher")) { Dispatchers.IO }
    factory { DocumentExporter(androidContext().contentResolver, get(named("ioDispatcher"))) }
}