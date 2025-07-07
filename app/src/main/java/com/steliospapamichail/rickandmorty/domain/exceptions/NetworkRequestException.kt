package com.steliospapamichail.rickandmorty.domain.exceptions

data class NetworkRequestException(private val msg:String) : Exception(msg)