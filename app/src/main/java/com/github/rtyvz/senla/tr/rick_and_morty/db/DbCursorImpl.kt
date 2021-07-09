package com.github.rtyvz.senla.tr.rick_and_morty.db

import android.database.Cursor
import com.github.rtyvz.senla.tr.dbapp.db.DbCursor

class DbCursorImpl(private val cursor: Cursor) : DbCursor {

    override fun getLong(fieldName: String): Long {
        val index = cursor.getColumnIndex(fieldName)
        return cursor.getLong(index)
    }

    override fun getString(fieldName: String): String {
        val index = cursor.getColumnIndex(fieldName)
        return cursor.getString(index)
    }

    override fun getDouble(fieldName: String): Double {
        val index = cursor.getColumnIndex(fieldName)
        return cursor.getDouble(index)
    }

    override fun next(): Boolean {
        return cursor.moveToNext()
    }

    override fun moveToFirst(): Boolean {
        return cursor.moveToFirst()
    }

    override fun closeCursor() {
        cursor.close()
    }
}