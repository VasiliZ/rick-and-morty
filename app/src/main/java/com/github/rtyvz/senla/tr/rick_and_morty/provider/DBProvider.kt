package com.github.rtyvz.senla.tr.rick_and_morty.provider

import com.github.rtyvz.senla.tr.rick_and_morty.App

object DBProvider {
    fun provideDb() = App.INSTANCE.db
}