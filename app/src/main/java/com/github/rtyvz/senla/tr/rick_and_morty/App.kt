package com.github.rtyvz.senla.tr.rick_and_morty

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import com.github.rtyvz.senla.tr.rick_and_morty.db.AppDb
import com.github.rtyvz.senla.tr.rick_and_morty.db.DbHelper
import com.github.rtyvz.senla.tr.rick_and_morty.network.CharactersApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {
    var state: State? = null
    lateinit var db: SQLiteDatabase
    private lateinit var okHttpClient: OkHttpClient

    companion object {
        lateinit var INSTANCE: App
        lateinit var api: CharactersApi
        private const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this
        db = AppDb(this).writableDatabase
        DbHelper().deleteCharactersLoadedTheDayBefore()
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