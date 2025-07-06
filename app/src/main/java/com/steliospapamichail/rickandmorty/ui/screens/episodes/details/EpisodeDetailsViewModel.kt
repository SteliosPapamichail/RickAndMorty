package com.steliospapamichail.rickandmorty.ui.screens.episodes.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steliospapamichail.rickandmorty.data.models.common.Resource
import com.steliospapamichail.rickandmorty.data.repositories.episodes.EpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel(
    private val episodeRepository: EpisodeRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<EpisodeDetailsUIState>(EpisodeDetailsUIState.Loading)
    val uiState = _uiState.asStateFlow()

    fun fetchEpisodeDetails(episodeId: Int) {
        viewModelScope.launch {
            episodeRepository.getEpisodeDetailsAsFlow(episodeId)
                .onEach { resource ->
                    _uiState.value = when (resource) {
                        is Resource.Error -> EpisodeDetailsUIState.Error(resource.throwable.message.toString())
                        Resource.Loading -> EpisodeDetailsUIState.Loading
                        is Resource.Success -> EpisodeDetailsUIState.Success(resource.data)
                    }
                }
                .catch { e ->
                    _uiState.value = EpisodeDetailsUIState.Error(e.message.toString())
                }
                .launchIn(viewModelScope)
        }
    }
}