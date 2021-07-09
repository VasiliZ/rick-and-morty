package com.github.rtyvz.senla.tr.rick_and_morty.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    @SerializedName("name")
    val name: String
) : Parcelable