package com.github.rtyvz.senla.tr.rick_and_morty.db.helpers

import android.database.sqlite.SQLiteDatabase

class CreateTableHelper {

    private val listWithPartsOfQuery = mutableListOf<String>()

    companion object {
        private const val CREATE_TABLE = "CREATE TABLE if not exists "
        private const val OPEN_BRACKET = "("
        private const val CLOSE_BRACKET = ")"
        private const val COMMA = ","
        private const val EMPTY_STRING = ""
    }

    fun setTableName(tableName: String) {
        listWithPartsOfQuery.add(CREATE_TABLE)
        listWithPartsOfQuery.add(tableName)
    }

    fun setPkField(nameField: String) {
        listWithPartsOfQuery.add(OPEN_BRACKET)
        listWithPartsOfQuery.add(" $nameField ")
        listWithPartsOfQuery.add(COMMA)
    }

    fun addField(nameField: String, typeField: String) {
        listWithPartsOfQuery.add(" $nameField $typeField")
        listWithPartsOfQuery.add(COMMA)
    }

    fun createTable(db: SQLiteDatabase?) {
        listWithPartsOfQuery.removeAt(listWithPartsOfQuery.size - 1)
        listWithPartsOfQuery.add(CLOSE_BRACKET)
        db?.execSQL(
            listWithPartsOfQuery.joinToString(
                separator = EMPTY_STRING,
                prefix = EMPTY_STRING,
                postfix = EMPTY_STRING
            )
        )
    }
}