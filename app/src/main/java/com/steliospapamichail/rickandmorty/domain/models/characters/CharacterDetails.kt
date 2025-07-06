package com.steliospapamichail.rickandmorty.domain.models.characters

import com.steliospapamichail.rickandmorty.domain.models.locations.LocationPreview
import com.steliospapamichail.rickandmorty.utils.Consts.NOT_AVAILABLE

data class CharacterDetails(
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationPreview,
    val location: LocationPreview,
    val imageUrl: String,
    val episodesCount: Int,
) {
    override fun toString(): String {
        return """
            Name: $name
            Status: ${status.withNotAvailableIfEmpty()}
            Species: ${species.withNotAvailableIfEmpty()}
            Type: ${type.withNotAvailableIfEmpty()}
            Gender: ${gender.withNotAvailableIfEmpty()}
            Origin: $origin
            Location: $location
            Appears in $episodesCount episodes
        """.trimIndent()
    }

    private fun String.withNotAvailableIfEmpty() = this.ifEmpty { NOT_AVAILABLE }
}