package com.steliospapamichail.rickandmorty.domain.exporters

import android.net.Uri

interface FileExporter {
    suspend fun export(uri: Uri, data: ByteArray)
}