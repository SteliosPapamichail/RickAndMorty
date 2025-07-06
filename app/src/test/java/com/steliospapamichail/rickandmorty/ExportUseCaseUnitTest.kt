package com.steliospapamichail.rickandmorty

import android.net.Uri
import com.steliospapamichail.rickandmorty.domain.exporters.DocumentExporter
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails
import com.steliospapamichail.rickandmorty.domain.models.locations.LocationPreview
import com.steliospapamichail.rickandmorty.domain.usecases.ExportCharacterUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.FileNotFoundException
import java.io.IOException

class ExportUseCaseUnitTest {
    private val fakeExporter = mockk<DocumentExporter>()
    private val useCase = ExportCharacterUseCase(fakeExporter)
    private val fakeUri = mockk<Uri>()
    private val dummyUriString = "content://downloads/rickandmorty"
    private val dummyCharacterDetails = CharacterDetails(
        "Rick", "Alive", "Human", "", "Male",
        origin = LocationPreview(id = 0, name = "Earth"),
        location = LocationPreview(id = 0, name = "CXP-41"),
        "",
        30
    )

    @Before
    fun setup() {
        mockkStatic(Uri::class)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `valid export returns success`() = runTest {
        every {
            Uri.parse(dummyUriString)
        } returns Uri.EMPTY

        coEvery { fakeExporter.export(fakeUri, any()) } returns Unit

        val result = useCase.execute(fakeUri, dummyCharacterDetails)

        assertTrue(result.isSuccess)

        val expectedCharacterBytes = dummyCharacterDetails.toString().toByteArray()
        coVerify(exactly = 1) {
            fakeExporter.export(fakeUri, expectedCharacterBytes)
        }
    }

    @Test
    fun `invalid export triggers IOException during write and returns failure`() = runTest {
        every {
            Uri.parse(dummyUriString)
        } returns Uri.EMPTY

        coEvery { fakeExporter.export(fakeUri, any()) } throws IOException()

        val result = useCase.execute(fakeUri, dummyCharacterDetails)

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is IOException)

        val expectedCharacterBytes = dummyCharacterDetails.toString().toByteArray()
        coVerify(exactly = 1) {
            fakeExporter.export(fakeUri, expectedCharacterBytes)
        }
    }

    @Test
    fun `invalid export triggers SecurityException during write and returns failure`() = runTest {
        every {
            Uri.parse(dummyUriString)
        } returns Uri.EMPTY

        coEvery { fakeExporter.export(fakeUri, any()) } throws SecurityException()

        val result = useCase.execute(fakeUri, dummyCharacterDetails)

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is SecurityException)

        val expectedCharacterBytes = dummyCharacterDetails.toString().toByteArray()
        coVerify(exactly = 1) {
            fakeExporter.export(fakeUri, expectedCharacterBytes)
        }
    }

    @Test
    fun `invalid export triggers FileNotFoundException during write and returns failure`() = runTest {
        every {
            Uri.parse(dummyUriString)
        } returns Uri.EMPTY

        coEvery { fakeExporter.export(fakeUri, any()) } throws FileNotFoundException()

        val result = useCase.execute(fakeUri, dummyCharacterDetails)

        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertNotNull(exception)
        assertTrue(exception is FileNotFoundException)

        val expectedCharacterBytes = dummyCharacterDetails.toString().toByteArray()
        coVerify(exactly = 1) {
            fakeExporter.export(fakeUri, expectedCharacterBytes)
        }
    }
}