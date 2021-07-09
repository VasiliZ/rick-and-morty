package com.github.rtyvz.senla.tr.rick_and_morty.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersApi {

    @GET("character")
    fun getCharacters(@Query(value = "page") pageId: Int): Call<Response>

    @GET("character/{id}")
    fun getCharacter(@Path("id") characterId:Long):Call<CharacterResponse>
}