package com.github.rtyvz.senla.tr.rick_and_morty.network

import com.github.rtyvz.senla.tr.rick_and_morty.entity.CharacterEntity
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
    val image: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("location")
    val location: Location
) {
    fun toCharacterEntity(): CharacterEntity {
        return CharacterEntity(id, image, name, gender, status, type, location.name)
    }
}
