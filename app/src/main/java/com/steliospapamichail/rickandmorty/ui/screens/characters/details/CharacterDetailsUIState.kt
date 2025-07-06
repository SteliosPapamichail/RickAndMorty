package com.steliospapamichail.rickandmorty.ui.screens.characters.details

import com.steliospapamichail.rickandmorty.domain.models.characters.CharacterDetails

sealed interface CharacterDetailsUIState {
    data object Loading : CharacterDetailsUIState
    data class Success(val details: CharacterDetails) : CharacterDetailsUIState
    data class Error(val msg: String) : CharacterDetailsUIState
}