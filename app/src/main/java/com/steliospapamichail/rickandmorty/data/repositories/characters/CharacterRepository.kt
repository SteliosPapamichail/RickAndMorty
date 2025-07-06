package com.steliospapamichail.rickandmorty.data.repositories.characters

import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails

interface CharacterRepository {
    suspend fun getCharacterDetails(charId:Int) : Result<CharacterDetails>
}