package com.steliospapamichail.rickandmorty.utils.formatting

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class DefaultEpisodeDateFormatter : EpisodeDateFormatter {
    private val inFmt = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.getDefault())
    private val outFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    override fun parse(text: String): LocalDate = LocalDate.parse(text, inFmt)

    override fun format(date: LocalDate): String = outFmt.format(date)
}