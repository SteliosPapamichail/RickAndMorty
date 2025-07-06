package com.steliospapamichail.rickandmorty.ui.screens.characters.details

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steliospapamichail.rickandmorty.R
import com.steliospapamichail.rickandmorty.data.repositories.characters.CharacterRepository
import com.steliospapamichail.rickandmorty.domain.usecases.ExportCharacterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val characterRepository: CharacterRepository,
    private val exportCharacterUseCase: ExportCharacterUseCase,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<CharacterDetailsEvent>()
    private val _uiState = MutableStateFlow<CharacterDetailsUIState>(CharacterDetailsUIState.Loading)
    val uiState = _uiState.asStateFlow()
    val uiEvents = _uiEvents.asSharedFlow()

    fun fetchCharacterDetails(charId: Int) {
        _uiState.value = CharacterDetailsUIState.Loading
        viewModelScope.launch {
            characterRepository.getCharacterDetails(charId)
                .onSuccess {
                    _uiState.value = CharacterDetailsUIState.Success(it)
                }
                .onFailure {
                    _uiState.value = CharacterDetailsUIState.Error(it.message.toString())
                }
        }
    }

    fun exportCharacter(destUri: Uri) {
        val state = _uiState.value
        if (state !is CharacterDetailsUIState.Success) {
            _uiEvents.tryEmit(CharacterDetailsEvent.ExportError(R.string.character_details_export_failure))
            return
        }
        viewModelScope.launch {
            exportCharacterUseCase.execute(destUri, state.details)
                .onSuccess {
                    _uiEvents.emit(CharacterDetailsEvent.ExportSuccess(R.string.character_details_export_success))
                }.onFailure {
                    Log.e(CharacterDetailsViewModel::class.simpleName, it.message.toString())
                    _uiEvents.emit(CharacterDetailsEvent.ExportError(R.string.character_details_export_failure))
                }
        }
    }
}