package com.steliospapamichail.rickandmorty.data.models.dtos.common


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationInfo(
    @SerialName("count")
    val count: Int,
    @SerialName("next")
    val next: String?,
    @SerialName("pages")
    val pages: Int,
    @SerialName("prev")
    val prev: String?
)