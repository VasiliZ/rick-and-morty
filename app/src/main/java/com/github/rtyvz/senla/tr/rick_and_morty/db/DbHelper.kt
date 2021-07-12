package com.github.rtyvz.senla.tr.rick_and_morty.db

import com.github.rtyvz.senla.tr.rick_and_morty.db.helpers.InsertValueHelper
import com.github.rtyvz.senla.tr.rick_and_morty.db.helpers.SelectDataHelper
import com.github.rtyvz.senla.tr.rick_and_morty.provider.DBProvider
import com.github.rtyvz.senla.tr.rick_and_morty.entity.CharacterEntity
import java.util.*

class DbHelper {

    companion object {
        private const val PERIOD_FOR_DELETE_1_DAY = "-1 day"
        private const val CURRENT_TIME = "now"
    }

    fun insertCharacter(character: CharacterEntity?) {
        if (character != null) {
            InsertValueHelper().apply {
                table("character")
                fields("character_id, url_image, name, gender, status,type, location, timestamp")
                values("(?,?,?,?,?,?,?,?)")
            }.insert(
                DBProvider.provideDb(), listOf<Any?>(
                    character.id,
                    character.image,
                    character.name,
                    character.gender,
                    character.status,
                    character.type,
                    character.location,
                    Date().time
                )
            )
        }
    }

    fun deleteCharactersLoadedTheDayBefore() {
        val sql =
            "Delete from character where timestamp <= date('$CURRENT_TIME', '$PERIOD_FOR_DELETE_1_DAY')"
        DBProvider.provideDb().execSQL(sql)
    }

    fun getCharacter(characterId: Long): CharacterEntity? {
        var character: CharacterEntity? = null
        val cursor = SelectDataHelper().apply {
            select("character.*")
            fromTables("character")
            where()
            condition("character_id = $characterId")
        }.select(DBProvider.provideDb())

        if (cursor.moveToFirst()) {
            do {
                character = CharacterEntity(
                    cursor.getLong("character_id"),
                    cursor.getString("url_image"),
                    cursor.getString("name"),
                    cursor.getString("gender"),
                    cursor.getString("status"),
                    cursor.getString("type"),
                    cursor.getString("location")
                )
            } while (cursor.next())
        }
        cursor.closeCursor()

        return character
    }
}