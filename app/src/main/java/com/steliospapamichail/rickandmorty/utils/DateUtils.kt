package com.steliospapamichail.rickandmorty.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.time.DateTimeException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun String.formatEpisodeAirDate(): String? {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val parsedDate = LocalDate.parse(this, inputFormatter)
        outputFormatter.format(parsedDate)
    } catch (dtpe: DateTimeParseException) {
        dtpe.printStackTrace()
        null
    } catch (dte: DateTimeException) {
        dte.printStackTrace()
        null
    }
}

fun Long?.formatTimestampToString(): String? {
    if (this == null) return null

    return try {
        val date = Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
        val hour  = date.hour.toString().padStart(2, '0')
        val min   = date.minute.toString().padStart(2, '0')
        return "${date.dayOfMonth}/${date.monthNumber}/${date.year} at ${hour}:${min}"
    } catch (dte: DateTimeException) {
        dte.printStackTrace()
        null
    }
}