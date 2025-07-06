package com.steliospapamichail.rickandmorty.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.steliospapamichail.rickandmorty.utils.Consts.NOT_AVAILABLE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class AppDataStore(private val appContext: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "data_sync")

    private val LAST_EPISODES_OVERVIEW_REFRESH = longPreferencesKey("last_episodes_overview_refresh")

    val lastRefreshFlow: Flow<String?> = appContext.dataStore.data
        .map { prefs ->
            val timestamp = prefs[LAST_EPISODES_OVERVIEW_REFRESH]
            timestamp?.formatTimestampToString() ?: NOT_AVAILABLE
        }

    suspend fun updateLastRefreshTimestamp(timeMillis: Long = Clock.System.now().toEpochMilliseconds()) {
        appContext.dataStore.edit { prefs ->
            prefs[LAST_EPISODES_OVERVIEW_REFRESH] = timeMillis
        }
    }
}