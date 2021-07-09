package com.github.rtyvz.senla.tr.rick_and_morty.ui.entity

import android.os.Parcelable
import com.github.rtyvz.senla.tr.rick_and_morty.network.Location
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterEntity(
    val id: Long? = null,
    val name: String? = null,
    val status: String? = null,
    val gender: String? = null,
    val image: String? = null,
    val type: String? = null,
    val location: Location? = null
) : Parcelable
