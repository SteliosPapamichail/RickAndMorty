package com.steliospapamichail.rickandmorty.ui.screens.characters.details

import androidx.annotation.StringRes

sealed class CharacterDetailsUIEvent {
    data class ExportSuccess(@StringRes val msgResId: Int) : CharacterDetailsUIEvent()
    data class ExportError(@StringRes val errorResId: Int) : CharacterDetailsUIEvent()
}