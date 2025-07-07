package com.steliospapamichail.rickandmorty.data.mappers

import com.steliospapamichail.rickandmorty.data.sources.local.db.entities.locations.LocationEntity
import com.steliospapamichail.rickandmorty.domain.models.locations.LocationPreview

fun LocationEntity?.toDomainModel() : LocationPreview? {
    return this?.let {  LocationPreview(it.id, it.name) }
}