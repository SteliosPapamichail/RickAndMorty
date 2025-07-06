package com.steliospapamichail.rickandmorty.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes msg: Int) {
    Toast.makeText(this, this.getString(msg), Toast.LENGTH_SHORT).show()
}