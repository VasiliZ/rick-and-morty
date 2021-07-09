package com.github.rtyvz.senla.tr.rick_and_morty.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.rtyvz.senla.tr.rick_and_morty.db.helpers.CreateTableHelper

class AppDb(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "app.db"
        private const val DB_VERSION = 1
        private const val CHARACTER_TABLE_NAME = "character"
        private const val TEXT_NOT_NULL_TYPE_FIELD = "TEXT NOT NULL"
        private const val TEXT_NULL_TYPE_FIELD = "TEXT"
        private const val INT_NULL_TYPE_FIELD = "INT NOT NULL"
        private const val LONG_NULL_TYPE_FIELD = "LONG NOT NULL"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        createTable(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    private fun createTable(db: SQLiteDatabase?) {
        CreateTableHelper().apply {
            setTableName(CHARACTER_TABLE_NAME)
            setPkField("character_id $INT_NULL_TYPE_FIELD PRIMARY KEY")
            addField("url_image", TEXT_NOT_NULL_TYPE_FIELD)
            addField("name", TEXT_NOT_NULL_TYPE_FIELD)
            addField("gender", TEXT_NOT_NULL_TYPE_FIELD)
            addField("status", TEXT_NOT_NULL_TYPE_FIELD)
            addField("type", TEXT_NULL_TYPE_FIELD)
            addField("location", TEXT_NULL_TYPE_FIELD)
            addField("timestamp", LONG_NULL_TYPE_FIELD)
        }.createTable(db)
    }
}