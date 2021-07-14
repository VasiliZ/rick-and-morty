package com.github.rtyvz.senla.tr.rick_and_morty.task

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import bolts.Continuation
import bolts.Task
import com.github.rtyvz.senla.tr.rick_and_morty.App
import com.github.rtyvz.senla.tr.rick_and_morty.db.DbHelper
import com.github.rtyvz.senla.tr.rick_and_morty.entity.CharacterEntity
import com.github.rtyvz.senla.tr.rick_and_morty.ui.particularCharacter.ParticularCharacterFragment

class GetCharacterTask {
    fun getCharacter(characterId: Long) {
        val localBroadcastManager = LocalBroadcastManager.getInstance(App.INSTANCE)
        Task.callInBackground {
            DbHelper().getCharacter(characterId)
        }.onSuccessTask(Continuation<CharacterEntity?, Task<Nothing>> {
            if (it.isFaulted || it.result == null) {
                GetParticularCharacterTask().getCharacter(characterId)
            } else {
                localBroadcastManager
                    .sendBroadcastSync(Intent(ParticularCharacterFragment.BROADCAST_SINGLE_CHARACTER)
                        .apply {
                            putExtra(
                                ParticularCharacterFragment.EXTRA_SINGLE_CHARACTER,
                                it.result
                            )
                        })
            }
            return@Continuation null
        }, Task.UI_THREAD_EXECUTOR)
    }
}