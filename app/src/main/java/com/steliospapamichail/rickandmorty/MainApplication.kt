package com.steliospapamichail.rickandmorty

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.BackoffPolicy
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.steliospapamichail.rickandmorty.di.appModule
import com.steliospapamichail.rickandmorty.utils.BackgroundWorkTags
import com.steliospapamichail.rickandmorty.workers.UpdateEpisodeListWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import java.util.concurrent.TimeUnit

private const val DATA_SYNC_UNIQUE_NAME = "updateEpisodeList"

class MainApplication : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(if (BuildConfig.DEBUG) Log.DEBUG else Log.ERROR)
            .build()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            // Log Koin into Android logger
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
        scheduleEpisodeDataSync()
    }

    /**
     * Schedules a periodic background task that is
     * responsible for updating the episodes list.
     *
     * @author Stelios Papamichail
     */
    private fun Context.scheduleEpisodeDataSync() {
        val syncConstraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true) // don't wanna waste the user's battery
            .setRequiredNetworkType(NetworkType.UNMETERED) // or their bandwidth :)
            .build()
        val syncWorkRequest = PeriodicWorkRequestBuilder<UpdateEpisodeListWorker>(30, TimeUnit.MINUTES)
            .setConstraints(syncConstraints)
            .addTag(BackgroundWorkTags.DATA_SYNC)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DATA_SYNC_UNIQUE_NAME,
            ExistingPeriodicWorkPolicy.UPDATE, //todo:sp change back to kEEP after validating
            syncWorkRequest
        )

        if (BuildConfig.DEBUG) { //note: adding a state listener to ease testing for reviewers, instead of having to wait 30 minutes
            ProcessLifecycleOwner.get()
                .let { owner ->
                    WorkManager.getInstance(this)
                        .getWorkInfoByIdLiveData(syncWorkRequest.id)
                        .observe(owner) { info ->
                            Log.d("WorkerState", "State: ${info?.state}")
                        }
                }
        }
    }
}