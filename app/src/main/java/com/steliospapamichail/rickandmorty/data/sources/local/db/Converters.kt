package com.steliospapamichail.rickandmorty.data.sources.local.db

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromIntList(ids: List<Int>?): String? =
        ids?.joinToString(separator = ",")

    @TypeConverter
    fun toIntList(data: String?): List<Int> =
        data
            ?.takeIf { it.isNotBlank() }
            ?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?: emptyList()
}
