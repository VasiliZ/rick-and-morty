package com.github.rtyvz.senla.tr.rick_and_morty.provider

import com.github.rtyvz.senla.tr.rick_and_morty.task.GetParticularCharacterTask
import com.github.rtyvz.senla.tr.rick_and_morty.task.LoadCharacterPageTask

object TasksProvider {
    fun provideTaskForLoadCharacters(pageId: Int) =
        LoadCharacterPageTask().fetchCharactersByPage(pageId)

    fun getParticularCharacter(characterId: Long) =
        GetParticularCharacterTask().getCharacter(characterId)
}