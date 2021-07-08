package com.github.rtyvz.senla.tr.rick_and_morty.network

import com.github.rtyvz.senla.tr.rick_and_morty.ui.entity.CharacterEntity
import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("image")
    val image: String
) {
    fun toCharacterEntity(): CharacterEntity {
        return CharacterEntity(id, name, status, gender, image)
    }
}