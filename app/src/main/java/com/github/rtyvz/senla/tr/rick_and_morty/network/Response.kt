package com.github.rtyvz.senla.tr.rick_and_morty.network

import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("info")
    val info:PageInfo,
    @SerializedName("results")
    val listData:List<CharacterResponse>
)