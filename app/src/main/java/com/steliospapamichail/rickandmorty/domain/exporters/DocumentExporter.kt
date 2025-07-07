package com.steliospapamichail.rickandmorty.domain.exporters

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.FileOutputStream
import java.io.IOException

class DocumentExporter(
    private val contentResolver: ContentResolver,
    private val coroutineDispatcher: CoroutineDispatcher,
) : FileExporter {

    /**
     * @throws IOException When the given data could not be written to the uri's fd.
     * @throws SecurityException if a security manager exists and its checkWrite method denies write access to the file descriptor
     * @throws FileNotFoundException  if no file exists under the URI or the mode is invalid.
     */
    override suspend fun export(uri: Uri, data: ByteArray): Unit = withContext(coroutineDispatcher) {
        contentResolver.openFileDescriptor(uri, "w")?.use { ostream ->
            FileOutputStream(ostream.fileDescriptor).use {
                it.write(data)
            }
        }
    }
}