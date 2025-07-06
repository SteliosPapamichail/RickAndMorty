package com.steliospapamichail.rickandmorty.domain.models.locations

import kotlinx.serialization.Serializable

@Serializable
data class LocationPreview(
    val id:Int,
    val name:String
) {
    override fun toString(): String {
        return name
    }
}
