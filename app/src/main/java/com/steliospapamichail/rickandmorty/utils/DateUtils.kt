package com.steliospapamichail.rickandmorty.utils

import com.steliospapamichail.rickandmorty.utils.formatting.DefaultEpisodeDateFormatter
import com.steliospapamichail.rickandmorty.utils.formatting.EpisodeDateFormatter
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.DateTimeException
import java.time.format.DateTimeParseException

fun String.formatEpisodeAirDate(
    formatter: EpisodeDateFormatter = DefaultEpisodeDateFormatter()
): String? {
    return try {
        val date = formatter.parse(this)
        formatter.format(date)
    } catch (dtpe: DateTimeParseException) {
        dtpe.printStackTrace()
        null
    } catch (dte: DateTimeException) {
        dte.printStackTrace()
        null
    }
}

fun Long?.formatTimestampToString(timezone: TimeZone = TimeZone.currentSystemDefault()): String? {
    if (this == null) return null

    return try {
        val date = Instant.fromEpochMilliseconds(this).toLocalDateTime(timezone)
        val hour  = date.hour.toString().padStart(2, '0')
        val min   = date.minute.toString().padStart(2, '0')
        return "${date.dayOfMonth}/${date.monthNumber}/${date.year} at ${hour}:${min}"
    } catch (dte: DateTimeException) {
        dte.printStackTrace()
        null
    }
}