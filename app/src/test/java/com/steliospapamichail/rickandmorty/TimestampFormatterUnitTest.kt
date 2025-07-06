package com.steliospapamichail.rickandmorty

import com.steliospapamichail.rickandmorty.utils.formatTimestampToString
import kotlinx.datetime.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.Instant

class TimestampFormatterUnitTest {
    @Test
    fun `null timestamp formats to null`() {
        assertEquals(null, (null as Long?).formatTimestampToString())
    }

    @Test
    fun `epoch zero formats to 1-1-1970 at 00-00 for UTC`() {
        val epochStart = 0L
        assertEquals("1/1/1970 at 00:00", epochStart.formatTimestampToString(TimeZone.UTC))
    }

    @Test
    fun `known timestamp formats correctly`() {
        val timestamp = Instant.parse("2025-07-06T12:49:15Z").toEpochMilli()
        assertEquals("6/7/2025 at 12:49", timestamp.formatTimestampToString(TimeZone.UTC))
    }
}