package com.steliospapamichail.rickandmorty.ui.nav

import androidx.annotation.Keep
import com.steliospapamichail.rickandmorty.R
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed interface Route {
    @Serializable
    data object EpisodesOverview : Route

    @Serializable
    data class EpisodeDetails(val id: Int) : Route

    @Serializable
    data class CharacterDetails(val id: Int) : Route

    companion object {
        fun getTitleFromQualifiedName(name: String?) =
            when (name) {
                EpisodesOverview::class.qualifiedName -> R.string.episodes_overview_title
                EpisodeDetails::class.qualifiedName -> R.string.episode_details_title
                CharacterDetails::class.qualifiedName -> R.string.character_details_title
                else -> R.string.app_name
            }
    }
}