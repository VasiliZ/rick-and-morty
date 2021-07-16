package com.github.rtyvz.senla.tr.rick_and_morty

import android.os.Parcelable
import com.github.rtyvz.senla.tr.rick_and_morty.entity.CharacterEntity

data class State(
    var isCharacterTaskRunning: Boolean = false,
    val characterEntityList: MutableList<CharacterEntity> = mutableListOf(),
    var characterRecyclerState: Parcelable? = null,
    var nextPage: Int = 1,
    var pageCount: Int = 0,
    var lastOpenedCharacterId: Long = 0L
)