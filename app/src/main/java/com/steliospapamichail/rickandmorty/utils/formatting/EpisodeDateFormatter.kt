package com.steliospapamichail.rickandmorty.utils.formatting

import java.time.LocalDate


interface EpisodeDateFormatter {
    fun parse(text: String): LocalDate
    fun format(date: LocalDate): String
}