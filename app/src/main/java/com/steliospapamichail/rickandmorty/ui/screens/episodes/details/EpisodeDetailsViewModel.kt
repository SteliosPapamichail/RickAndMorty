package com.steliospapamichail.rickandmorty.ui.screens.episodes.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steliospapamichail.rickandmorty.data.repositories.episodes.EpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel(
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<EpisodeDetailsUIState>(EpisodeDetailsUIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchEpisodeDetails(episodeId: Int) {
        _uiState.value = EpisodeDetailsUIState.Loading
        viewModelScope.launch {
            episodeRepository.getEpisodeDetails(episodeId).onSuccess {
                _uiState.value = EpisodeDetailsUIState.Success(details = it)
            }.onFailure {
                _uiState.value = EpisodeDetailsUIState.Error(it.message.toString())
            }
        }
    }
}