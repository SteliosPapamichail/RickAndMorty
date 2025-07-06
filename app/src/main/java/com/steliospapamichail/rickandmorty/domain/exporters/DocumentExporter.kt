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
     * @throws IOException When the given Uri's FileDescriptor could
     * not be retrieved or used.
     */
    override suspend fun export(uri: Uri, data: ByteArray): Unit = withContext(coroutineDispatcher) {
        contentResolver.openFileDescriptor(uri, "w")?.use { ostream ->
            FileOutputStream(ostream.fileDescriptor).use {
                it.write(data)
            }
        }
    }
}