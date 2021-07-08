package com.github.rtyvz.senla.tr.rick_and_morty

import android.app.Application
import com.github.rtyvz.senla.tr.rick_and_morty.network.CharactersApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    companion object {
        var state: State? = null
        lateinit var INSTANCE: App
        lateinit var api: CharactersApi
        private lateinit var okHttpClient: OkHttpClient
        private const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        okHttpClient = OkHttpClient.Builder().build()
        api = provideApi()
    }

    private fun provideApi() = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(CharactersApi::class.java)
}