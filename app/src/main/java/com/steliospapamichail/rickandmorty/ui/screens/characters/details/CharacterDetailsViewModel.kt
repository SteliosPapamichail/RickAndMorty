package com.steliospapamichail.rickandmorty.ui.screens.characters.details

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steliospapamichail.rickandmorty.R
import com.steliospapamichail.rickandmorty.data.models.shared.Resource
import com.steliospapamichail.rickandmorty.domain.repositories.character.CharacterRepository
import com.steliospapamichail.rickandmorty.domain.usecases.ExportCharacterUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val characterRepository: CharacterRepository,
    private val exportCharacterUseCase: ExportCharacterUseCase,
) : ViewModel() {
    private val _uiEvents = MutableSharedFlow<CharacterDetailsUIEvent>()
    private val _uiState = MutableStateFlow<CharacterDetailsUIState>(CharacterDetailsUIState.Loading)
    val uiState = _uiState.asStateFlow()
    val uiEvents = _uiEvents.asSharedFlow()

    fun fetchCharacterDetails(charId: Int) {
        viewModelScope.launch {
            characterRepository.getCharacterDetailsAsFlow(charId)
                .onEach { resource ->
                    _uiState.value = when (resource) {
                        is Resource.Error -> CharacterDetailsUIState.Error(resource.throwable.message.toString())
                        Resource.Loading -> CharacterDetailsUIState.Loading
                        is Resource.Success -> CharacterDetailsUIState.Success(resource.data)
                    }
                }
                .catch { e ->
                    _uiState.value = CharacterDetailsUIState.Error(e.message.toString())
                }
                .launchIn(viewModelScope)
        }
    }

    fun exportCharacter(destUri: Uri) {
        val state = _uiState.value
        if (state !is CharacterDetailsUIState.Success) {
            _uiEvents.tryEmit(CharacterDetailsUIEvent.ExportError(R.string.character_details_export_failure))
            return
        }
        viewModelScope.launch {
            exportCharacterUseCase.execute(destUri, state.details)
                .onSuccess {
                    _uiEvents.emit(CharacterDetailsUIEvent.ExportSuccess(R.string.character_details_export_success))
                }.onFailure {
                    Log.e(CharacterDetailsViewModel::class.simpleName, it.message.toString())
                    _uiEvents.emit(CharacterDetailsUIEvent.ExportError(R.string.character_details_export_failure))
                }
        }
    }
}