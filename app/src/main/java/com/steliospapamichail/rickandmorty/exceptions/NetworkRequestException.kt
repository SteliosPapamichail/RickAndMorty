package com.steliospapamichail.rickandmorty.exceptions

data class NetworkRequestException(private val msg:String) : Exception(msg)