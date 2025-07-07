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
                    debugLog("Episodes from response are $episodes")
                    db.withTransaction {
                        episodeDao.deleteByPage(pageNumber)
                        episodeDao.insertAll(episodes.map { EpisodePreviewEntity.fromDto(it, pageNumber) })
                    }
                    debugLog("Episodes persisted in DB")
                } else {
                    debugLog("Network request failed, retrying based on policy")
                    return@withContext Result.retry()
                }
            }
        } catch (ioEx: IOException) {
            debugLog(ioEx.message.toString())
            return@withContext Result.retry()
        } catch (t: Throwable) {
            debugLog(t.message.toString())
            return@withContext Result.failure()
        }

        debugLog("data sync succeeded!")
        return@withContext Result.success()
    }

    private fun debugLog(msg:String) = Log.d(UpdateEpisodeListWorker::class.simpleName, msg)
}