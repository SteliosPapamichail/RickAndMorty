package com.steliospapamichail.rickandmorty

import com.steliospapamichail.rickandmorty.utils.formatEpisodeAirDate
import com.steliospapamichail.rickandmorty.utils.formatting.EpisodeDateFormatter
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DateTimeException
import java.time.LocalDate

class DateFormattingUnitTest {
    @Test
    fun valid_episode_air_date_format_succeeds() {
        assertEquals("10/01/2013", "January 10, 2013".formatEpisodeAirDate())
    }

    @Test
    fun invalid_episode_air_date_format_datetime_parse_error_returns_null() {
        assertEquals(null, "lorem ipsum".formatEpisodeAirDate())
    }

    @Test
    fun invalid_episode_air_date_leap_year_returns_last_day_of_month() {
        //note: https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/time/format/ResolverStyle.html#SMART TIL : )
        assertEquals("28/02/2019", "February 29, 2019".formatEpisodeAirDate())
    }

    @Test
    fun invalid_episode_air_date_format_datetime_error_returns_null() {
        val badFormatter = object : EpisodeDateFormatter {
            override fun parse(text: String): LocalDate {
                return LocalDate.now()
            }

            override fun format(date: LocalDate): String {
                throw DateTimeException("")
            }

        }
        assertEquals(null, "".formatEpisodeAirDate(badFormatter))
    }
}