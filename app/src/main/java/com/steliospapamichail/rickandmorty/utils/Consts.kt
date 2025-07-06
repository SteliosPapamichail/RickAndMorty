package com.steliospapamichail.rickandmorty.utils

import java.util.concurrent.TimeUnit

object Consts {
    const val NOT_AVAILABLE = "N/A"
}

object FileExtensions {
    const val TXT_FILE_EXTENSION = ".txt"
    const val JSON_FILE_EXTENSION = ".json"
}

object MimeTypes {
    const val TXT_FILE_MIME_TYPE = "application/txt"
    const val JSON_FILE_MIME_TYPE = "application/json"
}

object BackgroundWorkTags {
    const val DATA_SYNC = "dataSync"
}

object DbRecordTTL {
    val RECORD_TTL = TimeUnit.MINUTES.toMillis(5)
}