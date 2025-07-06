package com.steliospapamichail.rickandmorty.data.models.common

/**
 * Represents the various states of a resource,
 * usually accessible by a repository class.
 * @author Stelios Papamichail
 * @since 06/07/2025
 */
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val throwable: Throwable) : Resource<Nothing>()
}