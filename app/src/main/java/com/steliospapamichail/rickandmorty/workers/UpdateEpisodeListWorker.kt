package com.steliospapamichail.rickandmorty.workers

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.steliospapamichail.rickandmorty.data.sources.local.db.AppDatabase
import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.episodes.EpisodePreviewEntity
import com.steliospapamichail.rickandmorty.data.sources.remote.api.EpisodeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.IOException

class UpdateEpisodeListWorker(appContext: Context, workerParameters: WorkerParameters) : CoroutineWorker(appContext, workerParameters),
    KoinComponent {

    private val db: AppDatabase by inject()
    private val episodeService: EpisodeService by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val savedPages = db.episodePageKeysDao().getAllSavedPages()
        debugLog("Total saved pages was $savedPages")
        val episodeDao = db.episodeDao()
        try {
            savedPages.forEach { pageNumber ->
                debugLog("Refreshing episodes for page $pageNumber")
                val response = episodeService.getAllEpisodes(page = pageNumber)

                if (response.isSuccessful) {
                    val episodes = response.body()?.episodes ?: return@withContext Result.retry()

                    db.withTransaction {
                        episodeDao.deleteByPage(pageNumber)
                        episodeDao.insertAll(episodes.map { EpisodePreviewEntity.fromDto(it, pageNumber) })
                    }
                } else {
                    return@withContext Result.retry()
                }
            }
        } catch (ioEx: IOException) {
            return@withContext Result.retry()
        } catch (t: Throwable) {
            return@withContext Result.failure()
        }

        return@withContext Result.success()
    }

    private fun debugLog(msg:String) = Log.d(UpdateEpisodeListWorker::class.simpleName, msg)
}