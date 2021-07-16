package com.github.rtyvz.senla.tr.rick_and_morty.network

import com.google.gson.annotations.SerializedName

data class PageInfo(
    @SerializedName("count")
    val count: Int,
    @SerializedName("pages")
    val pages: Int
)
