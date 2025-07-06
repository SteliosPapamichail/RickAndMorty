package com.steliospapamichail.rickandmorty.domain.models.episodes

data class EpisodeDetails(
    val airedOn:String,
    val characterIds:List<Int>,
    val episodeCode:String,
    val title:String,
)
