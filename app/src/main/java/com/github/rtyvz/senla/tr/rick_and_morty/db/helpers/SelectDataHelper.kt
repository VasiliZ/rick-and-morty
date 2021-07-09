package com.github.rtyvz.senla.tr.rick_and_morty.db.helpers

import android.database.sqlite.SQLiteDatabase
import com.github.rtyvz.senla.tr.dbapp.db.DbCursor
import com.github.rtyvz.senla.tr.rick_and_morty.db.DbCursorImpl

class SelectDataHelper {
    private val listWithPartOfQuery = mutableListOf<String>()

    companion object {
        private const val SELECT_OPERATOR = "SELECT "
        private const val FROM_OPERATOR = " FROM "
        private const val WHERE_OPERATOR = " WHERE "
        private const val AND_OPERATOR = " AND "
        private const val ORDER_BY_OPERATOR = " ORDER BY "
        private const val GROUP_BY_OPERATOR = " GROUP BY "
        private const val DESC_OPERATOR = " DESC "
        private const val ASC_OPERATOR = " ASC "
        private const val EMPTY_STRING = ""
    }

    fun select(fields: String) {
        listWithPartOfQuery.add(SELECT_OPERATOR)
        listWithPartOfQuery.add(fields)
    }

    fun fromTables(tables: String) {
        listWithPartOfQuery.add(FROM_OPERATOR)
        listWithPartOfQuery.add(tables)
    }

    fun where() {
        listWithPartOfQuery.add(WHERE_OPERATOR)
    }

    fun and() {
        listWithPartOfQuery.add(AND_OPERATOR)
    }

    fun condition(condition: String) {
        listWithPartOfQuery.add(condition)
    }

    fun groupBy(columnName: String){
        listWithPartOfQuery.add(GROUP_BY_OPERATOR)
        listWithPartOfQuery.add(columnName)
    }

    fun orderBy(columnName:String){
        listWithPartOfQuery.add(ORDER_BY_OPERATOR)
        listWithPartOfQuery.add(columnName)
    }

    fun desc(){
        listWithPartOfQuery.add(DESC_OPERATOR)
    }

    fun asc(){
        listWithPartOfQuery.add(ASC_OPERATOR)
    }

    fun select(db: SQLiteDatabase): DbCursor {
        return DbCursorImpl(
            db.rawQuery(
                listWithPartOfQuery.joinToString(
                    separator = EMPTY_STRING,
                    prefix = EMPTY_STRING,
                    postfix = EMPTY_STRING
                ), null
            )
        )
    }
}