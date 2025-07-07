package com.steliospapamichail.rickandmorty

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.steliospapamichail.rickandmorty.data.exporters.DocumentExporter
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails
import com.steliospapamichail.rickandmorty.domain.models.locations.LocationPreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DocumentExporterInstrumentedTest {
    private lateinit var context: Context
    private lateinit var resolver: ContentResolver
    private lateinit var exporter: DocumentExporter
    private val dummyCharacter: CharacterDetails = CharacterDetails(
        name = "Ricky",
        status = "Dead",
        species = "Alien",
        type = "",
        gender = "Who knows",
        origin = LocationPreview(id = 0, name = "Unknown"),
        location = LocationPreview(id = 1, name = "Nostromo"),
        imageUrl = "",
        episodesCount = 10
    )

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        resolver = context.contentResolver
        exporter = DocumentExporter(resolver, Dispatchers.IO)
    }

    @Test
    fun export_writes_data_to_real_file_URI() = runBlocking {
        val tmp = File.createTempFile("export_test", ".txt", context.cacheDir)
        tmp.deleteOnExit()
        val uri = Uri.fromFile(tmp)
        val charPayload = dummyCharacter.toString().toByteArray()

        exporter.export(uri, charPayload)

        val bytesReadFromTmpFile = tmp.readBytes()
        assertArrayEquals(charPayload, bytesReadFromTmpFile)
    }

    @Test(expected = FileNotFoundException::class)
    fun export_to_non_existent_URI_throws_FileNotFoundException() = runBlocking {
        val badFile = File(context.cacheDir, "invalidDir/invalidChar.txt")
        val badUri = Uri.fromFile(badFile)

        exporter.export(badUri, dummyCharacter.toString().toByteArray())
    }

    @Test(expected = IOException::class)
    fun export_to_read_only_file_throws_IOException() = runBlocking {
        val tmp = File.createTempFile("export_test_read_only", ".txt", context.cacheDir)
        tmp.deleteOnExit()
        tmp.setReadable(true, true)
        tmp.setWritable(false, true)
        val uri = Uri.fromFile(tmp)

        exporter.export(uri, dummyCharacter.toString().toByteArray())
    }
}