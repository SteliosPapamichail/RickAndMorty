package com.steliospapamichail.rickandmorty.ui.screens.characters.details

import androidx.annotation.StringRes

sealed class CharacterDetailsEvent {
    data class ExportSuccess(@StringRes val msgResId: Int) : CharacterDetailsEvent()
    data class ExportError(@StringRes val errorResId: Int) : CharacterDetailsEvent()
}