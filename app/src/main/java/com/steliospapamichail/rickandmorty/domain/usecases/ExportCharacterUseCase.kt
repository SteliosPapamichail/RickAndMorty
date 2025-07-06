package com.steliospapamichail.rickandmorty.domain.usecases

import android.net.Uri
import com.steliospapamichail.rickandmorty.domain.exporters.DocumentExporter
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails
import java.io.IOException

class ExportCharacterUseCase(
    private val documentExporter: DocumentExporter,
) {
    //note: I'm not a fun of overriding the invoke operator as imo, it doesn't communicate the execution intent
    suspend fun execute(destinationUri: Uri, characterDetails: CharacterDetails): Result<Unit> {
        return try {
            val charDetailsBytes = characterDetails.toString().toByteArray()
            documentExporter.export(destinationUri, charDetailsBytes)
            Result.success(Unit)
        } catch (ioEx: IOException) {
            Result.failure(ioEx)
        } catch (iaEx: IllegalArgumentException) {
            Result.failure(iaEx)
        }
    }
}