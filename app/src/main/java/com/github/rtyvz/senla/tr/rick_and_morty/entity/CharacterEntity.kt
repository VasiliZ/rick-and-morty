package com.github.rtyvz.senla.tr.rick_and_morty.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterEntity(
    val id: Long? = null,
    val image: String? = null,
    val name: String? = null,
    val gender: String? = null,
    val status: String? = null,
    val type: String? = null,
    val location: String? = null
) : Parcelable