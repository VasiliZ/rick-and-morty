package com.github.rtyvz.senla.tr.rick_and_morty.ui.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterEntity(
    val id: Long,
    val name: String,
    val status: String,
    val gender: String,
    val image: String
):Parcelable
