package com.github.rtyvz.senla.tr.rick_and_morty.db.helpers

import android.database.sqlite.SQLiteDatabase

class InsertValueHelper {
    private val listWithPartsOfQuery = mutableListOf<String>()

    companion object {
        private const val INSERT_OPERATION = "insert into "
        private const val OPEN_BRACKET = "("
        private const val CLOSE_BRACKET = ")"
        private const val VALUES = "VALUES"
        private const val SELECT_OPERATOR = " SELECT "
        private const val FROM_OPERATOR = " FROM "
    }

    fun table(tableName: String) {
        listWithPartsOfQuery.add(INSERT_OPERATION)
        listWithPartsOfQuery.add(tableName)
    }

    fun selectFields(selectFields: String) {
        listWithPartsOfQuery.add(selectFields)
    }

    fun fields(fieldsName: String) {
        listWithPartsOfQuery.add(OPEN_BRACKET)
        listWithPartsOfQuery.add(fieldsName)
        listWithPartsOfQuery.add(CLOSE_BRACKET)
    }

    fun values(values: String) {
        listWithPartsOfQuery.add(VALUES)
        listWithPartsOfQuery.add(values)
    }

    fun select() {
        listWithPartsOfQuery.add(SELECT_OPERATOR)
    }

    fun from(tableName: String) {
        listWithPartsOfQuery.add(FROM_OPERATOR)
        listWithPartsOfQuery.add(tableName)
    }

    fun insertFromSelect(db: SQLiteDatabase?) {
        val insertSql =
            listWithPartsOfQuery.joinToString(
                separator = "",
                prefix = "",
                postfix = ""
            )
        db?.execSQL(insertSql)
    }

    fun insert(db: SQLiteDatabase?, listArgs: List<Any?>) {
        val statement = db?.compileStatement(
            listWithPartsOfQuery.joinToString(
                separator = "",
                prefix = "",
                postfix = ""
            )
        )

        listArgs.forEachIndexed { index, value ->
            when (value) {
                is String -> statement?.bindString(index + 1, value.toString())
                is Int -> statement?.bindLong(index + 1, value.toLong())
                is Long -> statement?.bindLong(index + 1, value.toLong())
                is Double -> statement?.bindDouble(index + 1, value.toDouble())
                else -> statement?.bindNull(index + 1)
            }
        }
        statement?.executeInsert()
    }
}