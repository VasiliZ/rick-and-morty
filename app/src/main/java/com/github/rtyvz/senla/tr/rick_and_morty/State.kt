package com.github.rtyvz.senla.tr.rick_and_morty

import android.os.Parcelable
import com.github.rtyvz.senla.tr.rick_and_morty.ui.entity.CharacterEntity

data class State(
    var isCharacterTaskRunning: Boolean = false,
    val data: MutableList<CharacterEntity> = mutableListOf(),
    var characterRecyclerState: Parcelable? = null,
    var page: Int = 1,
    var pageCount: Int = 0
)