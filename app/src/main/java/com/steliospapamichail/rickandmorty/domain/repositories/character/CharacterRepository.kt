package com.steliospapamichail.rickandmorty.domain.repositories.character

import com.steliospapamichail.rickandmorty.data.models.shared.Resource
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacterDetailsAsFlow(charId:Int) : Flow<Resource<CharacterDetails>>
}