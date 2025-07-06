package com.steliospapamichail.rickandmorty.ui.screens.episodes.details

import androidx.compose.runtime.Immutable
import com.steliospapamichail.rickandmorty.domain.models.episodes.EpisodeDetails

sealed interface EpisodeDetailsUIState {
    data object Loading : EpisodeDetailsUIState
    data class Success(val details: EpisodeDetails) : EpisodeDetailsUIState
    data class Error(val msg: String) : EpisodeDetailsUIState
}
