package com.steliospapamichail.rickandmorty.data.repositories.characters

import com.steliospapamichail.rickandmorty.data.models.common.Resource
import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacterDetailsAsFlow(charId:Int) : Flow<Resource<CharacterDetails>>
}