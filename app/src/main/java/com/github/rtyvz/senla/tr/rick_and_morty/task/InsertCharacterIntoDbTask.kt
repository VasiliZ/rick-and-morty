package com.github.rtyvz.senla.tr.rick_and_morty.task

import bolts.Task
import com.github.rtyvz.senla.tr.rick_and_morty.db.DbHelper
import com.github.rtyvz.senla.tr.rick_and_morty.entity.CharacterEntity

class InsertCharacterIntoDbTask {
    fun insertCharacter(character: CharacterEntity?) {
        Task.callInBackground {
            DbHelper().insertCharacter(character)
        }
    }
}